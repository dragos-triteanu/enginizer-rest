package com.enginizer.security.jwt;

import com.enginizer.enums.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Filter class for parsing a JWT token and extracting the authentication information from the Authorization header.
 * This class parses the JWT token and
 */
public class JwtAuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private UserDetailsService userDetails;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletResponseWrapper httpResponseWrapper = new HttpServletResponseWrapper(httpResponse);

        String authToken = httpRequest.getHeader(tokenHeader);
        JwtTokenHolder tokenHolder = new JwtTokenHolder(authToken, TokenType.AUTH);

        // authToken.startsWith("Bearer ")
        // String authToken = header.substring(7);
        String username = jwtUtil.getMailFromToken(tokenHolder);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetails.loadUserByUsername(username);
                if (jwtUtil.validateToken(tokenHolder, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (UsernameNotFoundException ex){
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,ex.getMessage());
                return;
            }
        }

        if (authToken != null && jwtUtil.canTokenBeRefreshed(tokenHolder)) {
            String refreshedToken = jwtUtil.refreshToken(tokenHolder);
            httpResponseWrapper.setHeader("Authorization", refreshedToken);
        }

        chain.doFilter(request, httpResponse);
    }
}
