package com.enginizer.security.jwt;

import com.enginizer.enums.TokenType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by sorinavasiliu on 7/3/16.
 */
public class JwtTokenHolder implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    private TokenType tokenType;

    public JwtTokenHolder(String token, TokenType tokenType) {

        this.token = token;
        this.tokenType = tokenType;
    }

    public JwtTokenHolder(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    @JsonIgnore
    public TokenType getTokenType() {
        return tokenType;
    }

    @JsonIgnore
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }
}
