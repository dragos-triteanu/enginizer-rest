package com.enginizer.model;

/**
 * Enum class housing the available roles for the application.

 * ADMIN
 * USER
 * BUSINESS
 *
 * @author dragos.triteanu
 */
public enum Role {
    ADMIN("ADMIN"),
    USER("USER"),
    BUSINESS("BUSINESS");

    private final String role;

    Role(final String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }

}
