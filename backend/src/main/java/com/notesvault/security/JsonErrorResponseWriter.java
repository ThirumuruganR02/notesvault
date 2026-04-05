package com.notesvault.security;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Minimal JSON error bodies for servlet filter / security entry points (not {@code @RestControllerAdvice}).
 */
public final class JsonErrorResponseWriter {

    private JsonErrorResponseWriter() {}

    public static void write(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"" + escapeJson(message) + "\"}");
    }

    private static String escapeJson(String message) {
        if (message == null) {
            return "";
        }
        return message.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
