package com.gourav.restapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH = "BearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpringBoot REST API — Pets")
                        .version("1.0.0")
                        .description("""
                                A ready-to-use REST API template built with **Spring Boot 3 + MongoDB**.
                                
                                ## Authentication
                                1. Register via `POST /api/auth/signup`
                                2. Login via `POST /api/auth/login` — copy the `accessToken`
                                3. Click **Authorize** (🔒) above and enter `<your-token>` (without "Bearer ")
                                4. All protected endpoints will automatically include the token.
                                
                                ## Roles
                                | Role | Permissions |
                                |---|---|
                                | USER | Read pets |
                                | MODERATOR | Read + Update pets |
                                | ADMIN | Full CRUD |
                                """)
                        .contact(new Contact()
                                .name("saivamsikaruturi")
                                .url("https://github.com/saivamsikaruturi/SpringBootRestAPI"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://github.com/saivamsikaruturi/SpringBootRestAPI/blob/master/LICENSE")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH,
                                new SecurityScheme()
                                        .name(BEARER_AUTH)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT access token (from /api/auth/login)")));
    }
}
