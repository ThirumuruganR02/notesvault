package com.notesvault.common;

/**
 * Central HTTP path segments for controllers, security, CORS, and JWT filter bypass.
 */
public final class ApiPaths {

    public static final String AUTH = "/api/auth";
    public static final String NOTES = "/notes";

    /** Spring Security matcher. */
    public static final String AUTH_ANT = "/api/auth/**";

    /** OpenAPI + Swagger UI (springdoc). */
    public static final String[] DOC_PATHS = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};

    private ApiPaths() {}

    /** Servlet path is under {@value #AUTH} (e.g. login/register). */
    public static boolean isUnderAuth(String servletPath) {
        return servletPath.equals(AUTH) || servletPath.startsWith(AUTH + "/");
    }
}
