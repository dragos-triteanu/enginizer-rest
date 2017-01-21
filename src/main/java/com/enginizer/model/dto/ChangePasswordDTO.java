package com.enginizer.model.dto;

/**
 * Created by sorinavasiliu on 1/21/17.
 */
public class ChangePasswordDTO extends AuthenticationDTO {
    private String confirmPassword;

    public String getConfirmEmail() {
        return confirmPassword;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmPassword = confirmEmail;
    }
}
