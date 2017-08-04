package com.enginizer.security.jwt;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Paul on 6/19/2016.
 */
@Component
public class NoJwtPaths {

    private final ArrayList<String> noJwtPaths = new ArrayList<>();

    @Value("${enginizer.api.prefix}")
    private String apiPrefix;

    @PostConstruct
    public void init() {
        //for swagger
        noJwtPaths.add("/swagger");
        noJwtPaths.add("/webjars/");
        noJwtPaths.add("/images/");
        noJwtPaths.add("/configuration/");
        noJwtPaths.add("/v2/api-docs");
        noJwtPaths.add("/admin");
        noJwtPaths.add("/favicon.ico");
        noJwtPaths.add(apiPrefix + "/login");
        noJwtPaths.add(apiPrefix + "/register");
    }

    public boolean shouldBypassJWT(String servletPath) {
        for (String noJwtPath : noJwtPaths) {
            if (servletPath.contains(noJwtPath)) {
                return true;
            }
        }
        return false;
    }
}
