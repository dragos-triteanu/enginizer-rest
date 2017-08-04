//package com.enginizer.security.withProvider;
//
//import com.enginizer.enums.TokenType;
//import com.enginizer.security.auth.JWTTokenAuth;
//import com.enginizer.security.jwt.JWTTokenHolder;
//import java.io.IOException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//
///**
// * Filter class for parsing a JWT token and extracting the authentication information from the Authorization header.
// * This class parses the JWT token and
// */
//public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {
//    private static final String BEARER_SUFFIX = "Bearer ";
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//
//    public JwtAuthenticationTokenFilter(String filterURL) {
//        super(filterURL);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//        throws AuthenticationException, IOException, ServletException {
//        String tokenPayload = request.getHeader(AUTHORIZATION_HEADER);
//        JWTTokenHolder token = new JWTTokenHolder(tokenPayload, TokenType.AUTH);
//        return getAuthenticationManager().authenticate(new JWTTokenAuth(token));
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
//        Authentication authResult) throws IOException, ServletException {
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authResult);
//        SecurityContextHolder.setContext(context);
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//        AuthenticationException failed) throws IOException, ServletException {
//        SecurityContextHolder.clearContext();
//    }
//
//}
