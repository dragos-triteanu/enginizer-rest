package com.enginizer.service;

import com.enginizer.model.Role;
import com.enginizer.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling authentication.
 */
@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByEmailAddress(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return this.getUserDetails(user);
        }
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + role));
        return grantedAuths;
    }

    private org.springframework.security.core.userdetails.User getUserDetails(User user){
        return new org.springframework.security.core.userdetails.User
                (user.getEmail(),user.getPassword(),this.mapToGrantedAuthorities(user.getRole()));
    }

}
