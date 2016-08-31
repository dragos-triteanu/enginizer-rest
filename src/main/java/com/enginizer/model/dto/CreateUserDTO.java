package com.enginizer.model.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xPku on 8/31/16.
 */
public class CreateUserDTO extends AuthenticationDTO {

    @ApiModelProperty(required = true, value = "The first name of a user" , example = "Mike" , dataType = "string")
    private String firstName;

    @ApiModelProperty(required = true, value = "The last name of a user" , example = "Mike" , dataType = "string")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
