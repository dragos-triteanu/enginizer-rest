package com.enginizer.model.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xPku on 8/31/16.
 */
public class AuthenticationDTO {

    @ApiModelProperty(required = true, value = "The email of a user" , example = "admin@mail.com")
    protected String email;
    @ApiModelProperty(required = true, value = "The password of a user", example = "admin")
    protected String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
