package com.enginizer.resources.api;

import com.enginizer.model.dto.UserDTO;
import com.enginizer.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST resource for exposing an API for managing a User entity.
 */
@Api(value = "API", basePath = "api/user")
@RestController
public class UserResource {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Get all users", notes = "Retrieves a list of users by specific search criteria")
    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<?> getUserDetails() {
        List<UserDTO> users = userService.getUsers();

        return ResponseEntity.ok(users);
    }
}
