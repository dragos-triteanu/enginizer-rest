//package com.enginizer.security.withProvider;
//
//import com.enginizer.security.auth.JWTTokenAuth;
//import com.enginizer.security.jwt.JWTTokenHolder;
//import com.enginizer.security.jwt.JWTUtil;
//import java.util.List;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//
///**
// * An {@link AuthenticationProvider} implementation that will use provided
// * instance of {@link JwtToken} to perform authentication.
// *
// * @author vladimir.stankovic
// *
// * Aug 5, 2016
// */
//@SuppressWarnings("unchecked")
//public class JwtAuthenticationProvider implements AuthenticationProvider {
//
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetails;
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        JWTTokenHolder tokenHolder = (JWTTokenHolder) authentication.getCredentials();
//
//
//        // String authToken = header.substring(7);
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
//        String username = jwtUtil.getEmailFromToken(tokenHolder);
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            try {
//                UserDetails userDetails = this.userDetails.loadUserByUsername(username);
//                if (jwtUtil.validateToken(tokenHolder, userDetails.getUsername())) {
//                    authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }catch (UsernameNotFoundException ex){
//                authentication = null;
//            }
//        }
//
//
//        return usernamePasswordAuthenticationToken;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return (JWTTokenAuth.class.isAssignableFrom(authentication));
//    }
//}
