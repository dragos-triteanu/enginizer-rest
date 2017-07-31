package com.enginizer.resources.auth;

import com.enginizer.enums.TokenType;
import com.enginizer.model.dto.AuthenticationDTO;
import com.enginizer.model.dto.ChangePasswordDTO;
import com.enginizer.model.dto.CreateUserDTO;
import com.enginizer.model.entities.User;
import com.enginizer.security.jwt.JWTTokenHolder;
import com.enginizer.security.jwt.JWTUtil;
import com.enginizer.service.EmailService;
import com.enginizer.service.UserService;
import io.swagger.annotations.*;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * REST resource that exposes an api for authenticating via the server.
 */
@Api(value = "Authentication")
@RestController
public class AuthenticationResource {

    @Value("${jwt.header}")
    private String authenticationHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userServiceDetails;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "Authenticate", notes = "Provides authentication for a user to use the API.")
    @RequestMapping(value = "${route.authentication.login}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> authenticate(@RequestBody @ApiParam(name = "auth", required = true) AuthenticationDTO auth, Device device)
            throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userServiceDetails.loadUserByUsername(auth.getEmail());

        final String token = jwtUtil.generateToken(userDetails, device);

        return ResponseEntity.ok(new JWTTokenHolder(token));
    }

    @ApiOperation(value = "Register", notes = "Provides ability so that user can register.")
    @RequestMapping(value = "api/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody @ApiParam(name = "user", required = true) CreateUserDTO user,
                                      Device device) {
        if (StringUtils.isEmpty(user.getPassword()) || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required.");
        }

        if (StringUtils.isEmpty(user.getEmail()) || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mail is required.");
        }

        User userDetails = userService.findUserByEmailAddress(user.getEmail());

        if (userDetails != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The email is already used for other account.");
        }

        userService.createAccount(user);

        AuthenticationDTO auth = new AuthenticationDTO();
        auth.setEmail(user.getEmail());
        auth.setPassword(user.getPassword());

        return this.authenticate(auth, device);
    }

    @RequestMapping(value = "${route.authentication.password.forgot}", method = RequestMethod.POST)
    public ResponseEntity<?> forgotPassword(@RequestBody @ApiParam(name = "auth", required = true) AuthenticationDTO auth, Device device) {

        User userDetails = userService.findUserByEmailAddress(auth.getEmail());
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }

        String token = jwtUtil.generateForgotPasswordToken(userDetails, device);
        try {
            emailService.prepareAndSend(userDetails, token);
        } catch (MessagingException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending mail.");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "api/password/reset", method = RequestMethod.GET)
    public ResponseEntity<?> resetPassword(@RequestParam("token") @ApiParam(name = "token") String token)
    {
        JWTTokenHolder jwtToken = new JWTTokenHolder(token, TokenType.PASSWORD);

        if(!jwtUtil.validateToken(jwtToken,null)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or token expired.");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "api/password/reset", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody @ApiParam(name="changePassword") ChangePasswordDTO changePassword, Device device)
    {
        User userDetails = userService.findUserByEmailAddress(changePassword.getEmail());

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email is not valid.");
        }

        if(!changePassword.getPassword().equals(changePassword.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password are not equals.");
        }

        userDetails.setPassword(changePassword.getPassword());
        userDetails.encryptPassword();
        userService.updateUserPassword(userDetails);

        AuthenticationDTO auth = new AuthenticationDTO();
        auth.setEmail(changePassword.getEmail());
        auth.setPassword(changePassword.getPassword());

        return this.authenticate(auth, device);
    }

}
