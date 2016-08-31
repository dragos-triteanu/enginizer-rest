package com.enginizer.model.entities;

import com.enginizer.model.Role;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Entity class for modeling a user, in the db 'user' table.
 */
@Entity
@Table(name = "user")
@PrimaryKeyJoinColumn(name = "userId", referencedColumnName = "id")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(name = "Identifier of the user")
    private int id;

    @Column(name = "mail", nullable = false)
    @Email(message = "client.email.notEmail")
    private String mail;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "lastLogin")
    private Timestamp lastLogin;

    @Column(name = "createdOn")
    private Timestamp createdOn;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "socialMediaUserId")
    private String socialMediaUserId;

    @Column(name = "notificationsEnable")
    private boolean notificationsEnable = false;

    @PrePersist
    public void encryptPassword(){
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
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

    public String getSocialMediaUserId() {
        return socialMediaUserId;
    }

    public void setSocialMediaUserId(String socialMediaUserId) {
        this.socialMediaUserId = socialMediaUserId;
    }

    public boolean isNotificationsEnable() {
        return notificationsEnable;
    }

    public void setNotificationsEnable(boolean notificationsEnable) {
        this.notificationsEnable = notificationsEnable;
    }


}
