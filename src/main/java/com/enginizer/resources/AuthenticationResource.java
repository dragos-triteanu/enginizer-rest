package com.enginizer.resources;

import com.enginizer.model.entities.User;
import com.enginizer.security.jwt.JwtToken;
import com.enginizer.security.jwt.JwtTokenUtil;
import com.enginizer.security.jwt.JwtUser;
import com.enginizer.service.AuthenticationService;
import com.enginizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sorinavasiliu on 7/3/16.
 */
@RestController
public class AuthenticationResource {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationService userDetailsService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user, Device device)
            throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getMail(),user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = userService.findUserByEmailAddress(user.getMail());
        final String token = jwtTokenUtil.generateToken(userDetails, device);

        return ResponseEntity.ok(new JwtToken(token));
    }

    @RequestMapping(value = "${jwt.route.authentication.createAccount}", method = RequestMethod.POST)
    public ResponseEntity<?> createNewAccount(@RequestBody User user, Device device) {
        if (StringUtils.isEmpty(user.getPassword()) || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required.");
        }

        if (StringUtils.isEmpty(user.getMail()) || user.getMail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mail is required.");
        }

        User userDetails = userService.findUserByEmailAddress(user.getMail());

        if (userDetails != null) {
            if (user.getSocialMediaUserId() != null) {
                return createAuthenticationToken(user, device);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The email is already used for other account.");
            }
        }

        userDetails = userService.createAccount(user);
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The account couldn't be created.");
        }

        final String token = jwtTokenUtil.generateToken(userDetails, device);
        return ResponseEntity.ok(new JwtToken(token));
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getMailFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtToken(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
