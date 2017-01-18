package com.enginizer.resources;

import com.enginizer.model.dto.UserDTO;
import com.enginizer.security.jwt.JwtUtil;
import com.enginizer.service.UserService;
import com.enginizer.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST resource for exposing an API for managing a User entity.
 */
@RestController
public class UserResource {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "${route.user.details}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserDetails() {
        List<UserDTO> users = userService.getUsers();

        return ResponseEntity.ok(users);
    }
}
