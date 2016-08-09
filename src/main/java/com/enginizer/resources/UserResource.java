package com.enginizer.resources;

import com.enginizer.config.SecurityConfig;
import com.enginizer.model.dto.UserDTO;
import com.enginizer.model.entities.User;
import com.enginizer.security.jwt.JwtTokenUtil;
import com.enginizer.security.jwt.JwtUser;
import com.enginizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sorinavasiliu on 7/20/16.
 */
@RestController
public class UserResource {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "${route.user.details}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not found");
        }

        if (!jwtTokenUtil.validateToken(token, SecurityConfig.getCurrentUser())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String mail = jwtTokenUtil.getMailFromToken(token);
        UserDTO user = userService.findUserDtoByEmailAddress(mail);

        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "${route.user.update.info}", method = RequestMethod.POST)
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request, @RequestBody User user) {
        String token = request.getHeader(tokenHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not found");
        }

        UserDetails userDetails = SecurityConfig.getCurrentUser();
        if (!jwtTokenUtil.validateToken(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        user.setId(((JwtUser) userDetails).getId().intValue());
        userService.updateUserInfo(user);

        return ResponseEntity.status(HttpStatus.OK).body("User info updated");
    }
}
