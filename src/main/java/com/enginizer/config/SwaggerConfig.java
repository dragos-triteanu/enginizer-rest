package com.enginizer.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ApplicationObjectSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by xPku on 8/30/16.
 */

@EnableSwagger2
@Configuration
public class SwaggerConfig extends ApplicationObjectSupport {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.enginizer.resources.auth"))
                .paths(Predicates.not(authenticationPaths()))
                .build()
                .apiInfo(apiInfo())
                .groupName("Authentication");

    }

    @Bean
    public Docket authenticationDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.enginizer.resources.api"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .groupName("API")
                .globalOperationParameters(
                        Arrays.asList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Authorization Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("Header")
                                .defaultValue("Bearer ")
                                .required(true)
                                .build()));
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Enginizer REST")
                .version("1.0")
                .build();

    }

    private Predicate<String> authenticationPaths() {
        return or(
                regex("/api/passwordrecovery.*"));
    }
}

