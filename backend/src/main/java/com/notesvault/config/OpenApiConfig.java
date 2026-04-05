package com.notesvault.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_JWT = "bearer-jwt";

    @Bean
    public OpenAPI notesVaultOpenApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("NotesVault API")
                                .description(
                                        "Secure notes API. Obtain a JWT from **Authentication** endpoints, then click **Authorize** and enter `Bearer <your-token>` (or only the token, depending on UI) for note operations.")
                                .version("1.0.0"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        BEARER_JWT,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("JWT returned in `token` from login/register")));
    }
}
