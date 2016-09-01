package com.enginizer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.Device;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by xPku on 8/30/16.
 */

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("${swagger.enabled}")
    private boolean isSwaggerEnabled;

    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(Device.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.enginizer.resources")).build().enable(isSwaggerEnabled)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Enginizer REST")
                .description("Spring REST template project")
                .version("1.0")

                .build();
    }

}
