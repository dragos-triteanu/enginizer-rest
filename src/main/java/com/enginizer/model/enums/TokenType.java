package com.enginizer.model.enums;

/**
 * Created by sorinavasiliu on 1/20/17.
 */
public enum TokenType {
    AUTH("AUTH"),
    PASSWORD("PASSWORD");

    private final String token;

    TokenType(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token;
    }

}
