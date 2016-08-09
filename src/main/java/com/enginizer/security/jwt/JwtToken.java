package com.enginizer.security.jwt;

import java.io.Serializable;

/**
 * Created by sorinavasiliu on 7/3/16.
 */
public class JwtToken implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
