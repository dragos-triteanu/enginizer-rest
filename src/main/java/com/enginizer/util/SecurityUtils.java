package com.enginizer.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by xPku on 8/30/16.
 */
public class SecurityUtils {

    public static UserDetails getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User ?
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
    }
}
