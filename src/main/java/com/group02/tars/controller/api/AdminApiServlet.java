package com.group02.tars.controller.api;

import com.group02.tars.model.Application;
import com.group02.tars.model.Job;
import com.group02.tars.model.User;
import com.group02.tars.storage.FileStorage;
import com.group02.tars.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminApiServlet extends BaseApiServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User current = requireSessionUser(req, resp, "admin");
        if (current == null) return;

        String path = normalizePath(req);
        try {
            switch (path) {
                case "/dashboard" -> handleDashboard(resp);
                case "/users" -> handleUsers(req, resp);
                case "/applications" -> handleApplications(req, resp);
                case "/workload" -> handleWorkload(req, resp);
                default -> JsonResponse.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "SYSTEM_NOT_FOUND", "Endpoint not found.", req.getRequestURI());
            }
        } catch (Exception ex) {
            writeUnknownError(req, resp, ex);
        }
    }

    private void handleDashboard(HttpServletResponse resp) throws IOException {
        FileStorage storage = registry.storage();
        List<User> users = storage.loadUsers();
        List<Job> jobs = storage.loadJobs();
        List<Application> applications = storage.loadApplications();
        Map<String, User> userById = users.stream().collect(LinkedHashMap::new, (m, u) -> m.put(u.userId, u), Map::putAll);
        Map<String, Job> jobById = jobs.stream().collect(LinkedHashMap::new, (m, j) -> m.put(j.jobId, j), Map::putAll);

        List<Map<String, Object>> recent = applications.stream()
            .sorted(Comparator.comparing((Application a) -> safe(a.updatedAt)).reversed())
            .limit(5)
            .map(a -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("applicationId", a.applicationId);
                row.put("userId", a.userId);
                row.put("jobId", a.jobId);
                row.put("status", a.status);
                row.put("updatedAt", a.updatedAt);
                row.put("applicantName", userById.containsKey(a.userId) ? userById.get(a.userId).name : "Unknown");
                row.put("title", jobById.containsKey(a.jobId) ? safe(jobById.get(a.jobId).title) : "Unknown");
                return row;
            })
            .toList();

        List<Map<String, Object>> workload = buildWorkload(users, jobs, applications);
        List<Map<String, Object>> overloadUsers = workload.stream()
            .filter(row -> "overload".equals(row.get("riskLevel")))
            .toList();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalUsers", users.size());
        data.put("openJobs", jobs.stream().filter(j -> !"closed".equalsIgnoreCase(safe(j.status))).count());
        data.put("totalApplications", applications.size());
        data.put("overloadCount", overloadUsers.size());
        data.put("recentApplications", recent);
        data.put("overloadUsers", overloadUsers);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String role = safe(req.getParameter("role")).toLowerCase(Locale.ROOT);
        String keyword = safe(req.getParameter("keyword")).toLowerCase(Locale.ROOT);
        int page = queryInt(req, "page", 1);
        int size = queryInt(req, "size", 8);

        FileStorage storage = registry.storage();
        List<User> users = storage.loadUsers().stream()
            .filter(u -> role.isBlank() || role.equalsIgnoreCase(safe(u.role)))
            .filter(u -> keyword.isBlank() || (safe(u.name) + " " + safe(u.email) + " " + safe(u.userId)).toLowerCase(Locale.ROOT).contains(keyword))
            .toList();

        int validPage = Math.max(1, page);
        int validSize = Math.max(1, size);
        int totalItems = users.size();
        int totalPages = Math.max(1, (int) Math.ceil(totalItems / (double) validSize));
        int from = Math.min((validPage - 1) * validSize, totalItems);
        int to = Math.min(from + validSize, totalItems);
        List<User> rows = users.subList(from, to).stream().map(User::safeCopy).toList();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("users", rows);
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("page", validPage);
        meta.put("size", validSize);
        meta.put("totalItems", totalItems);
        meta.put("totalPages", totalPages);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, meta);
    }

    private void handleApplications(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String status = safe(req.getParameter("status"));
        String module = safe(req.getParameter("module"));
        String keyword = safe(req.getParameter("keyword")).toLowerCase(Locale.ROOT);

        FileStorage storage = registry.storage();
        List<Application> applications = storage.loadApplications();
        Map<String, User> userById = storage.loadUsers().stream().collect(LinkedHashMap::new, (m, u) -> m.put(u.userId, u), Map::putAll);
        Map<String, Job> jobById = storage.loadJobs().stream().collect(LinkedHashMap::new, (m, j) -> m.put(j.jobId, j), Map::putAll);

        List<Map<String, Object>> rows = new ArrayList<>();
        for (Application app : applications) {
            User user = userById.get(app.userId);
            Job job = jobById.get(app.jobId);
            String moduleName = job == null ? "-" : safe(job.moduleName);
            String title = job == null ? "Unknown" : safe(job.title);
            String applicantName = user == null ? "Unknown" : safe(user.name);

            if (!status.isBlank() && !status.equalsIgnoreCase(safe(app.status))) continue;
            if (!module.isBlank() && !module.equalsIgnoreCase(moduleName)) continue;
            String blob = (applicantName + " " + title).toLowerCase(Locale.ROOT);
            if (!keyword.isBlank() && !blob.contains(keyword)) continue;

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("applicationId", app.applicationId);
            row.put("userId", app.userId);
            row.put("jobId", app.jobId);
            row.put("status", app.status);
            row.put("reviewNote", safe(app.reviewNote));
            row.put("updatedAt", app.updatedAt);
            row.put("applicantName", applicantName);
            row.put("title", title);
            row.put("moduleName", moduleName);
            rows.add(row);
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("applications", rows);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleWorkload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String riskLevel = safe(req.getParameter("riskLevel")).toLowerCase(Locale.ROOT);

        FileStorage storage = registry.storage();
        List<Map<String, Object>> rows = buildWorkload(storage.loadUsers(), storage.loadJobs(), storage.loadApplications());
        if (!riskLevel.isBlank()) {
            rows = rows.stream().filter(row -> riskLevel.equals(row.get("riskLevel"))).toList();
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("workload", rows);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private List<Map<String, Object>> buildWorkload(List<User> users, List<Job> jobs, List<Application> applications) {
        Map<String, Job> jobById = jobs.stream().collect(LinkedHashMap::new, (m, j) -> m.put(j.jobId, j), Map::putAll);
        List<Application> selectedApps = applications.stream()
            .filter(a -> "selected".equalsIgnoreCase(safe(a.status)))
            .toList();

        List<Map<String, Object>> rows = new ArrayList<>();
        for (User user : users) {
            if (!"ta".equalsIgnoreCase(safe(user.role))) continue;
            List<Application> mine = selectedApps.stream().filter(a -> user.userId.equals(a.userId)).toList();
            int totalHours = mine.stream()
                .map(a -> jobById.get(a.jobId))
                .filter(j -> j != null && j.weeklyHours != null)
                .mapToInt(j -> j.weeklyHours)
                .sum();
            String risk = totalHours >= 28 ? "overload" : totalHours >= 20 ? "warning" : "normal";

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("userId", user.userId);
            row.put("name", user.name);
            row.put("selectedModules", mine.size());
            row.put("totalHours", totalHours);
            row.put("riskLevel", risk);
            rows.add(row);
        }
        rows.sort(Comparator.comparing((Map<String, Object> row) -> Integer.parseInt(String.valueOf(row.get("totalHours")))).reversed());
        return rows;
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizePath(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null || path.isBlank()) return "/";
        return path;
    }
}
