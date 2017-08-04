package com.enginizer.security.jwt;

import com.enginizer.enums.TokenType;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter class for parsing a JWT token and extracting the authentication information from the Authorization header.
 * This class parses the JWT token and
 */
@Component
@Order
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_SUFFIX = "Bearer ";

  @Autowired
  private UserDetailsService userDetails;

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private NoJwtPaths noJwtPaths;

  @Override
  protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain)
      throws ServletException, IOException {
    HttpServletResponseWrapper httpResponseWrapper = new HttpServletResponseWrapper(httpResponse);

    if (noJwtPaths.shouldBypassJWT(httpRequest.getServletPath())) {
      filterChain.doFilter(httpRequest, httpResponse);
      return;
    }

    String authToken = httpRequest.getHeader(AUTHORIZATION_HEADER);

    if(StringUtils.isEmpty(authToken) || !authToken.startsWith(BEARER_SUFFIX)){
      httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
      return;
    }

    authToken = authToken.substring(BEARER_SUFFIX.length());
    JWTTokenHolder tokenHolder = new JWTTokenHolder(authToken, TokenType.AUTH);

    try {
      parseJWTFromToken(httpRequest, httpResponseWrapper, tokenHolder);
    }catch (Exception e){
      httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
      return;
    }

    filterChain.doFilter(httpRequest, httpResponse);
  }

  private void parseJWTFromToken(HttpServletRequest httpRequest, HttpServletResponseWrapper httpResponseWrapper,
      JWTTokenHolder tokenHolder) {
    String username = jwtUtil.getEmailFromToken(tokenHolder);
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetails.loadUserByUsername(username);
        if (jwtUtil.validateToken(tokenHolder, userDetails.getUsername())) {
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
              userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    if (jwtUtil.canTokenBeRefreshed(tokenHolder)) {
      String refreshedToken = jwtUtil.refreshToken(tokenHolder);
      httpResponseWrapper.setHeader(AUTHORIZATION_HEADER, refreshedToken);
    }
  }
}
