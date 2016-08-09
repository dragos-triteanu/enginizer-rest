package com.enginizer.model;

/**
 * Enum class housing the available authorities for the application.
 * These authorities are as follows:
 * SUPPORTER(default) - the type of user that can access the site, and support project.
 * MODERATOR(admin) - a user that accepts/denies project and that moderates the site.
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
