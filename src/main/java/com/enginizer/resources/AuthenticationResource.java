package com.enginizer.resources;

import com.enginizer.model.dto.AuthenticationDTO;
import com.enginizer.model.dto.CreateUserDTO;
import com.enginizer.model.entities.User;
import com.enginizer.security.jwt.JwtTokenHolder;
import com.enginizer.security.jwt.JwtUtil;
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

import javax.servlet.http.HttpServletRequest;

/**
 * REST resource that exposes an api for authenticating via the server.
 */
@Api(value = "Authentication", basePath = "/api")
@RestController
public class AuthenticationResource {

    @Value("${jwt.header}")
    private String authenticationHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil JwtUtil;

    @Autowired
    private UserDetailsService userServiceDetails;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Authenticate", notes = "Provides authentication for a user to use the API.")
    @RequestMapping(value = "${route.authentication.login}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> authenticate(@RequestBody @ApiParam(name = "auth", required = true) AuthenticationDTO auth, Device device)
            throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userServiceDetails.loadUserByUsername(auth.getEmail());

        final String token = JwtUtil.generateToken(userDetails, device);

        return ResponseEntity.ok(new JwtTokenHolder(token));
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

       return this.authenticate(auth,device);
    }

    @ApiOperation(hidden = true, value = "Refresh")
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(authenticationHeader);

        if (JwtUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = JwtUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtTokenHolder(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
