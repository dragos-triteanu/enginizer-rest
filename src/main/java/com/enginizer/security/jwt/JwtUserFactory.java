package com.enginizer.security.jwt;

import com.enginizer.model.entities.User;
import com.enginizer.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.*;

/**
 * Created by sorinavasiliu on 7/3/16.
 */
public final class JWTUserFactory {
    private JWTUserFactory() {
    }

    public static JWTUser create(User user) {
        return new JWTUser(
                new Long(user.getId()),
                user.getMail(),
                user.getMail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRole()),
                true,
                null
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + role));
        return grantedAuths;
    }
}
