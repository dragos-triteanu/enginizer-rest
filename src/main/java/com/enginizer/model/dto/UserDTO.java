package com.enginizer.model.dto;


/**
 * Created by sorinavasiliu on 7/20/16.
 */
public class UserDTO {
    private int userId;
    private String userName;
    private String firstName;
    private String lastName;
    private boolean notificationsEnable;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public boolean isNotificationsEnable() {
        return notificationsEnable;
    }

    public void setNotificationsEnable(boolean notificationsEnable) {
        this.notificationsEnable = notificationsEnable;
    }
}
