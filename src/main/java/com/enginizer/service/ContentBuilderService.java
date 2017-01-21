package com.enginizer.service;

import com.enginizer.model.entities.User;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.net.URI;

/**
 * Created by sorinavasiliu on 1/21/17.
 */
@Service
public class ContentBuilderService {

    private static final String EMAIL_SIMPLE_TEMPLATE_NAME = "mailTemplate";

    @Autowired
    private TemplateEngine templateEngine;

    public String build(User user, String token) {
        // Prepare the evaluation context
        final Context context = new Context();
        context.setVariable("name", user.getFirstName());
        context.setVariable("link", getUrlForForgotPassword(token));

        return templateEngine.process(EMAIL_SIMPLE_TEMPLATE_NAME, context);
    }

    private String getUrlForForgotPassword(String token) {
        try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("localhost")
                    .setPort(8080)
                    .setPath("/api/password/reset")
                    .setParameter("token", token)
                    .build();
            return uri.toString();
        } catch (Exception ex){
            return null;
        }
    }
}
