//package com.enginizer.security.withProvider;
//
//import com.enginizer.security.jwt.JWTTokenHolder;
//import java.util.Collection;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//
///**
// * Created by drago on 8/4/2017.
// */
//public class JWTTokenAuth extends AbstractAuthenticationToken {
//
//  private String username;
//  private JWTTokenHolder jwtTokenHolder;
//
//  public JWTTokenAuth(JWTTokenHolder unsafeToken) {
//    super(null);
//    this.jwtTokenHolder = unsafeToken;
//    this.setAuthenticated(false);
//  }
//
//  public JWTTokenAuth(String username, Collection<? extends GrantedAuthority> authorities) {
//    super(authorities);
//    this.eraseCredentials();
//    this.username = username;
//    super.setAuthenticated(true);
//  }
//
//  @Override
//  public void setAuthenticated(boolean authenticated) {
//    if (authenticated) {
//      throw new IllegalArgumentException(
//          "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
//    }
//    super.setAuthenticated(false);
//  }
//
//  @Override
//  public Object getCredentials() {
//    return jwtTokenHolder;
//  }
//
//  @Override
//  public Object getPrincipal() {
//    return this.username;
//  }
//
//  @Override
//  public void eraseCredentials() {
//    super.eraseCredentials();
//    this.username= null;
//  }
//}
