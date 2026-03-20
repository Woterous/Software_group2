package com.group02.tars.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Minimal JSON envelope writer for stub API responses.
 */
public final class JsonResponse {

    private JsonResponse() {
    }

    public static void writeError(HttpServletResponse resp, int status, String code, String message, String path) throws IOException {
        resp.setStatus(status);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");

        String payload = "{" +
            "\"success\":false," +
            "\"data\":null," +
            "\"meta\":{\"path\":\"" + escape(path) + "\"}," +
            "\"error\":{" +
            "\"code\":\"" + escape(code) + "\"," +
            "\"message\":\"" + escape(message) + "\"," +
            "\"details\":[]" +
            "}" +
            "}";

        resp.getWriter().write(payload);
    }

    private static String escape(String input) {
        if (input == null) {
            return "";
        }
        return input
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }
}
