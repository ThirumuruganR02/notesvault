package com.notesvault.config;

import com.notesvault.common.CorsConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        for (String pattern : CorsConstants.MAPPINGS) {
            var registration =
                    registry.addMapping(pattern)
                            .allowedOrigins(CorsConstants.ALLOWED_ORIGINS)
                            .allowedMethods(CorsConstants.ALLOWED_METHODS)
                            .allowedHeaders("*")
                            .allowCredentials(true);
            if (CorsConstants.exposesAuthorizationHeader(pattern)) {
                registration.exposedHeaders("Authorization");
            }
        }
    }
}
