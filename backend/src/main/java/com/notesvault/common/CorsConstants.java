package com.notesvault.common;

/**
 * Browser CORS settings shared across API and note routes.
 */
public final class CorsConstants {

    public static final String[] ALLOWED_ORIGINS = {"http://localhost:5173", "http://localhost:3000"};

    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

    /** Patterns registered with {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer}. */
    public static final String[] MAPPINGS = {"/api/**", ApiPaths.NOTES, ApiPaths.NOTES + "/**"};

    private CorsConstants() {}

    public static boolean exposesAuthorizationHeader(String pattern) {
        return pattern.startsWith(ApiPaths.NOTES);
    }
}
