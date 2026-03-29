package com.group02.tars.controller.api;

import com.group02.tars.model.Application;
import com.group02.tars.model.Job;
import com.group02.tars.model.User;
import com.group02.tars.service.ServiceException;
import com.group02.tars.storage.FileStorage;
import com.group02.tars.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MoApiServlet extends BaseApiServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User current = requireSessionUser(req, resp, "mo");
        if (current == null) return;

        String path = normalizePath(req);
        try {
            switch (path) {
                case "/dashboard" -> handleDashboard(resp, current.userId);
                case "/jobs" -> handleJobs(req, resp, current.userId);
                case "/applicants" -> handleApplicants(req, resp);
                default -> {
                    if (path.startsWith("/review/")) {
                        handleReview(resp, path.substring("/review/".length()));
                    } else {
                        JsonResponse.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "SYSTEM_NOT_FOUND", "Endpoint not found.", req.getRequestURI());
                    }
                }
            }
        } catch (ServiceException ex) {
            writeServiceError(req, resp, ex);
        } catch (Exception ex) {
            writeUnknownError(req, resp, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User current = requireSessionUser(req, resp, "mo");
        if (current == null) return;

        String path = normalizePath(req);
        if (!"/jobs".equals(path)) {
            JsonResponse.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "SYSTEM_NOT_FOUND", "Endpoint not found.", req.getRequestURI());
            return;
        }

        try {
            Map<String, Object> body = readBodyAsMap(req);
            FileStorage storage = registry.storage();
            List<Job> jobs = storage.loadJobs();

            Job job = new Job();
            job.jobId = nextId("JOB", jobs.stream().map(j -> j.jobId).toList());
            job.title = required(asString(body, "title"), "title");
            job.moduleName = required(asString(body, "moduleName"), "moduleName");
            job.requiredSkills = required(asString(body, "requiredSkills"), "requiredSkills");
            job.deadline = required(asString(body, "deadline"), "deadline");
            job.description = required(asString(body, "description"), "description");
            job.status = enumOrDefault(asString(body, "status"), List.of("open", "closing", "closed"), "open");
            job.postedBy = current.userId;
            job.weeklyHours = toIntOrDefault(asString(body, "weeklyHours"), 6);
            job.createdAt = LocalDate.now().toString();

            jobs.add(job);
            storage.saveJobs(jobs);

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("job", job);
            JsonResponse.writeSuccess(resp, HttpServletResponse.SC_CREATED, data, null);
        } catch (ServiceException ex) {
            writeServiceError(req, resp, ex);
        } catch (Exception ex) {
            writeUnknownError(req, resp, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User current = requireSessionUser(req, resp, "mo");
        if (current == null) return;

        String path = normalizePath(req);
        try {
            if (path.startsWith("/jobs/")) {
                handleUpdateJob(req, resp, current.userId, path.substring("/jobs/".length()));
                return;
            }
            if (path.startsWith("/applications/") && path.endsWith("/status")) {
                String appId = path.substring("/applications/".length(), path.length() - "/status".length());
                handleUpdateApplicationStatus(req, resp, appId);
                return;
            }
            JsonResponse.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "SYSTEM_NOT_FOUND", "Endpoint not found.", req.getRequestURI());
        } catch (ServiceException ex) {
            writeServiceError(req, resp, ex);
        } catch (Exception ex) {
            writeUnknownError(req, resp, ex);
        }
    }

    private void handleDashboard(HttpServletResponse resp, String moUserId) throws IOException {
        FileStorage storage = registry.storage();
        List<Job> jobs = storage.loadJobs();
        List<Application> applications = storage.loadApplications();

        List<Job> myJobs = jobs.stream().filter(j -> moUserId.equals(j.postedBy)).toList();
        List<String> myJobIds = myJobs.stream().map(j -> j.jobId).toList();
        List<Application> myApps = applications.stream().filter(a -> myJobIds.contains(a.jobId)).toList();

        List<Job> nearDeadline = myJobs.stream()
            .filter(j -> !"closed".equalsIgnoreCase(safe(j.status)))
            .sorted(Comparator.comparing(j -> safe(j.deadline)))
            .limit(5)
            .toList();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("activeJobs", myJobs.stream().filter(j -> !"closed".equalsIgnoreCase(safe(j.status))).count());
        data.put("totalApplicants", myApps.size());
        data.put("pendingReview", myApps.stream().filter(a -> "pending".equalsIgnoreCase(safe(a.status))).count());
        data.put("selectedCount", myApps.stream().filter(a -> "selected".equalsIgnoreCase(safe(a.status))).count());
        data.put("nearDeadline", nearDeadline);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleJobs(HttpServletRequest req, HttpServletResponse resp, String moUserId) throws IOException {
        String status = safe(req.getParameter("status"));
        String keyword = safe(req.getParameter("keyword")).toLowerCase(Locale.ROOT);

        FileStorage storage = registry.storage();
        List<Job> jobs = storage.loadJobs();
        List<Application> apps = storage.loadApplications();

        List<Map<String, Object>> rows = new ArrayList<>();
        for (Job job : jobs) {
            if (!moUserId.equals(job.postedBy)) continue;
            if (!status.isBlank() && !status.equalsIgnoreCase(safe(job.status))) continue;
            String blob = (safe(job.title) + " " + safe(job.moduleName)).toLowerCase(Locale.ROOT);
            if (!keyword.isBlank() && !blob.contains(keyword)) continue;

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("jobId", job.jobId);
            row.put("title", job.title);
            row.put("moduleName", job.moduleName);
            row.put("requiredSkills", job.requiredSkills);
            row.put("deadline", job.deadline);
            row.put("description", job.description);
            row.put("status", job.status);
            row.put("postedBy", job.postedBy);
            row.put("weeklyHours", job.weeklyHours);
            row.put("createdAt", job.createdAt);
            row.put("applicantCount", apps.stream().filter(a -> job.jobId.equals(a.jobId)).count());
            rows.add(row);
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("jobs", rows);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleApplicants(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jobId = safe(req.getParameter("jobId"));
        String status = safe(req.getParameter("status"));
        String keyword = safe(req.getParameter("keyword")).toLowerCase(Locale.ROOT);

        FileStorage storage = registry.storage();
        List<Application> applications = storage.loadApplications();
        Map<String, User> userById = storage.loadUsers().stream().collect(LinkedHashMap::new, (m, u) -> m.put(u.userId, u), Map::putAll);
        Map<String, Job> jobById = storage.loadJobs().stream().collect(LinkedHashMap::new, (m, j) -> m.put(j.jobId, j), Map::putAll);

        List<Map<String, Object>> rows = new ArrayList<>();
        for (Application app : applications) {
            Job job = jobById.get(app.jobId);
            User user = userById.get(app.userId);
            if (job == null || user == null) continue;
            if (!jobId.isBlank() && !jobId.equals(app.jobId)) continue;
            if (!status.isBlank() && !status.equalsIgnoreCase(safe(app.status))) continue;

            String blob = (safe(user.name) + " " + safe(job.title) + " " + safe(job.moduleName)).toLowerCase(Locale.ROOT);
            if (!keyword.isBlank() && !blob.contains(keyword)) continue;

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("applicationId", app.applicationId);
            row.put("userId", app.userId);
            row.put("jobId", app.jobId);
            row.put("status", app.status);
            row.put("reviewNote", safe(app.reviewNote));
            row.put("updatedAt", app.updatedAt);
            row.put("applicantName", user.name);
            row.put("applicantSkills", String.join(", ", user.skills == null ? List.of() : user.skills));
            row.put("cvPath", safe(user.cvPath));
            row.put("title", safe(job.title));
            row.put("moduleName", safe(job.moduleName));
            rows.add(row);
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("applicants", rows);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleReview(HttpServletResponse resp, String applicationId) throws IOException, ServiceException {
        FileStorage storage = registry.storage();
        Application app = storage.loadApplications().stream()
            .filter(a -> applicationId.equals(a.applicationId))
            .findFirst()
            .orElseThrow(() -> new ServiceException(HttpServletResponse.SC_NOT_FOUND, "APPLICATION_NOT_FOUND", "Application not found."));

        User user = storage.loadUsers().stream().filter(u -> app.userId.equals(u.userId)).findFirst().orElse(null);
        Job job = storage.loadJobs().stream().filter(j -> app.jobId.equals(j.jobId)).findFirst().orElse(null);

        Map<String, Object> row = new LinkedHashMap<>();
        row.put("applicationId", app.applicationId);
        row.put("userId", app.userId);
        row.put("jobId", app.jobId);
        row.put("status", app.status);
        row.put("reviewNote", safe(app.reviewNote));
        row.put("updatedAt", app.updatedAt);
        row.put("applicantName", user == null ? "Unknown" : user.name);
        row.put("applicantSkills", user == null || user.skills == null ? List.of() : user.skills);
        row.put("cvPath", user == null ? "" : safe(user.cvPath));
        row.put("title", job == null ? "Unknown" : safe(job.title));
        row.put("moduleName", job == null ? "-" : safe(job.moduleName));
        row.put("requiredSkills", job == null ? "" : safe(job.requiredSkills));

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("application", row);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleUpdateJob(HttpServletRequest req, HttpServletResponse resp, String moUserId, String jobId) throws IOException, ServiceException {
        Map<String, Object> body = readBodyAsMap(req);
        FileStorage storage = registry.storage();
        List<Job> jobs = storage.loadJobs();
        Job job = jobs.stream().filter(j -> jobId.equals(j.jobId)).findFirst()
            .orElseThrow(() -> new ServiceException(HttpServletResponse.SC_NOT_FOUND, "JOB_NOT_FOUND", "Job not found."));

        if (!moUserId.equals(job.postedBy)) {
            throw new ServiceException(HttpServletResponse.SC_FORBIDDEN, "JOB_PERMISSION_DENIED", "MO cannot modify this job.");
        }

        String title = safe(asString(body, "title"));
        String moduleName = safe(asString(body, "moduleName"));
        String requiredSkills = safe(asString(body, "requiredSkills"));
        String deadline = safe(asString(body, "deadline"));
        String description = safe(asString(body, "description"));
        String status = safe(asString(body, "status"));
        String weeklyHours = safe(asString(body, "weeklyHours"));

        if (!title.isBlank()) job.title = title;
        if (!moduleName.isBlank()) job.moduleName = moduleName;
        if (!requiredSkills.isBlank()) job.requiredSkills = requiredSkills;
        if (!deadline.isBlank()) job.deadline = deadline;
        if (!description.isBlank()) job.description = description;
        if (!status.isBlank()) job.status = enumOrDefault(status, List.of("open", "closing", "closed"), job.status);
        if (!weeklyHours.isBlank()) job.weeklyHours = toIntOrDefault(weeklyHours, job.weeklyHours == null ? 6 : job.weeklyHours);

        storage.saveJobs(jobs);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("job", job);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleUpdateApplicationStatus(HttpServletRequest req, HttpServletResponse resp, String appId) throws IOException, ServiceException {
        Map<String, Object> body = readBodyAsMap(req);
        String status = safe(asString(body, "status")).toLowerCase(Locale.ROOT);
        if (!List.of("selected", "rejected").contains(status)) {
            throw new ServiceException(422, "APPLICATION_STATUS_INVALID", "Status must be selected or rejected.");
        }

        FileStorage storage = registry.storage();
        List<Application> applications = storage.loadApplications();
        Application app = applications.stream().filter(a -> appId.equals(a.applicationId)).findFirst()
            .orElseThrow(() -> new ServiceException(HttpServletResponse.SC_NOT_FOUND, "APPLICATION_NOT_FOUND", "Application not found."));

        app.status = status;
        app.reviewNote = safe(asString(body, "reviewNote"));
        app.updatedAt = LocalDate.now().toString();
        storage.saveApplications(applications);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("application", app);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private String required(String value, String field) throws ServiceException {
        if (safe(value).isBlank()) {
            throw new ServiceException(422, "VALIDATION_REQUIRED_FIELD", "Field " + field + " is required.");
        }
        return value.trim();
    }

    private String enumOrDefault(String value, List<String> allowed, String defaultValue) {
        String normalized = safe(value).toLowerCase(Locale.ROOT);
        if (allowed.contains(normalized)) return normalized;
        return defaultValue;
    }

    private int toIntOrDefault(String raw, int defaultValue) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizePath(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null || path.isBlank()) return "/";
        return path;
    }

    private String nextId(String prefix, List<String> ids) {
        int max = 0;
        for (String id : ids) {
            if (id == null || !id.startsWith(prefix)) continue;
            try {
                max = Math.max(max, Integer.parseInt(id.substring(prefix.length())));
            } catch (Exception ignored) {
            }
        }
        return prefix + String.format("%03d", max + 1);
    }
}
