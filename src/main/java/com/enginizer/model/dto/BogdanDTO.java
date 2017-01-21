package com.enginizer.model.dto;

import com.enginizer.model.Role;

/**
 * Created by badmotherfucker on 1/21/17.
 */
public class BogdanDTO {

    private String bogdanName;
    private String bogdanPassword;
    private Role bogdanRole;


    public Role getBogdanRole() {
        return bogdanRole;
    }

    public void setBogdanRole(Role bogdanRole) {
        this.bogdanRole = bogdanRole;
    }

    public String getBogdanName() {
        return bogdanName;
    }

    public void setBogdanName(String bogdanName) {
        this.bogdanName = bogdanName;
    }

    public String getBogdanPassword() {
        return bogdanPassword;
    }

    public void setBogdanPassword(String bogdanPassword) {
        this.bogdanPassword = bogdanPassword;
    }
}
