package com.group02.tars.controller.api;

import com.group02.tars.model.User;
import com.group02.tars.service.ServiceException;
import com.group02.tars.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AuthApiServlet extends BaseApiServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isPath(req, "/me")) {
            handleMe(req, resp);
            return;
        }
        JsonResponse.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "SYSTEM_NOT_FOUND", "Endpoint not found.", req.getRequestURI());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isPath(req, "/login")) {
            handleLogin(req, resp);
            return;
        }
        if (isPath(req, "/register")) {
            handleRegister(req, resp);
            return;
        }
        JsonResponse.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "SYSTEM_NOT_FOUND", "Endpoint not found.", req.getRequestURI());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isPath(req, "/logout")) {
            handleLogout(req, resp);
            return;
        }
        JsonResponse.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "SYSTEM_NOT_FOUND", "Endpoint not found.", req.getRequestURI());
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Map<String, Object> body = readBodyAsMap(req);
            User user = registry.userService().register(
                asString(body, "name"),
                asString(body, "email"),
                asString(body, "password"),
                asString(body, "role"),
                asString(body, "skills"),
                asString(body, "cvPath")
            );

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("user", user);
            JsonResponse.writeSuccess(resp, HttpServletResponse.SC_CREATED, data, null);
        } catch (ServiceException ex) {
            writeServiceError(req, resp, ex);
        } catch (Exception ex) {
            writeUnknownError(req, resp, ex);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Map<String, Object> body = readBodyAsMap(req);
            User user = registry.userService().login(
                asString(body, "email"),
                asString(body, "password"),
                asString(body, "role")
            );

            HttpSession session = req.getSession(true);
            session.setAttribute(SESSION_USER_ID, user.userId);
            session.setAttribute(SESSION_ROLE, user.role);

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("user", user);
            JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
        } catch (ServiceException ex) {
            writeServiceError(req, resp, ex);
        } catch (Exception ex) {
            writeUnknownError(req, resp, ex);
        }
    }

    private void handleMe(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User sessionUser = requireSessionUser(req, resp, "ta", "mo", "admin");
        if (sessionUser == null) return;

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("user", sessionUser);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loggedOut", true);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private boolean isPath(HttpServletRequest req, String expected) {
        String path = req.getPathInfo();
        if (path == null || path.isBlank()) {
            path = "/";
        }
        return expected.equals(path);
    }
}
