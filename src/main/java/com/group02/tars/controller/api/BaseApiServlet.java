package com.group02.tars.controller.api;

import com.group02.tars.util.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Common API behavior for stub endpoints in Sprint 2.
 */
public abstract class BaseApiServlet extends HttpServlet {

    protected void notImplemented(HttpServletRequest req, HttpServletResponse resp, String scope) throws IOException {
        JsonResponse.writeError(
            resp,
            HttpServletResponse.SC_NOT_IMPLEMENTED,
            "SYSTEM_NOT_IMPLEMENTED",
            "Stub endpoint: " + scope + " is reserved for backend integration.",
            req.getRequestURI()
        );
    }
}
