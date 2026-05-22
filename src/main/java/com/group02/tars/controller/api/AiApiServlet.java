package com.group02.tars.controller.api;

import com.group02.tars.entity.AiConversation;
import com.group02.tars.entity.Application;
import com.group02.tars.entity.User;
import com.group02.tars.service.ServiceException;
import com.group02.tars.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * API servlet for AI assistant endpoints under {@code /api/v1/ai/*}.
 */
public class AiApiServlet extends BaseApiServlet {

    /**
     * Handles the AI provider status endpoint.
     *
     * @param req current request
     * @param resp current response
     * @throws IOException if a response cannot be written
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User current = requireSessionUser(req, resp, "ta", "mo", "admin");
        if (current == null) return;

        String path = normalizePath(req);
        try {
            if ("/status".equals(path)) {
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("provider", registry.aiAssistantService().providerStatus());
                JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
                return;
            }
            if ("/conversations".equals(path)) {
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("conversations", registry.aiConversationService().listConversations(current.userId));
                JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
                return;
            }
            if (path.startsWith("/conversations/")) {
                String sessionId = path.substring("/conversations/".length());
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("conversation", registry.aiConversationService().getConversation(current.userId, sessionId));
                JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
                return;
            }
            if ("/artifacts/latest".equals(path)) {
                AiConversation.AiArtifact artifact = registry.aiConversationService().latestArtifact(
                    current.userId,
                    req.getParameter("type"),
                    req.getParameter("scopeKey")
                );
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("artifact", artifact);
                JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
                return;
            }
            JsonResponse.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "SYSTEM_NOT_FOUND", "Endpoint not found.", req.getRequestURI());
        } catch (ServiceException ex) {
            writeServiceError(req, resp, ex);
        } catch (Exception ex) {
            writeUnknownError(req, resp, ex);
        }
    }

    /**
     * Dispatches AI assistant actions such as chat, recommendations, summaries, and risk analysis.
     *
     * @param req current request
     * @param resp current response
     * @throws IOException if a response cannot be written
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = normalizePath(req);
        try {
            switch (path) {
                case "/chat" -> handleChat(req, resp);
                case "/action/execute" -> handleActionExecute(req, resp);
                case "/ta/job-recommendations" -> handleTaJobRecommendations(req, resp);
                case "/mo/candidate-summary" -> handleMoCandidateSummary(req, resp);
                case "/admin/risk-analysis" -> handleAdminRiskAnalysis(req, resp);
                default -> JsonResponse.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "SYSTEM_NOT_FOUND", "Endpoint not found.", req.getRequestURI());
            }
        } catch (ServiceException ex) {
            writeServiceError(req, resp, ex);
        } catch (Exception ex) {
            writeUnknownError(req, resp, ex);
        }
    }

    private void handleTaJobRecommendations(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        User current = requireSessionUser(req, resp, "ta");
        if (current == null) return;
        Map<String, Object> body = readBodyAsMap(req);
        AiConversation conversation = registry.aiConversationService().getOrCreateConversation(
            current.userId,
            current.role,
            firstNonBlank(asString(body, "page"), "ta/jobs"),
            asString(body, "sessionId")
        );
        Map<String, Object> data = registry.aiAssistantService().recommendJobsForTa(current.userId);
        persistAssistantOutput(conversation.sessionId, "TA_RECOMMENDATION", "ta/jobs", current.userId, data);
        data.put("sessionId", conversation.sessionId);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleChat(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        User current = requireSessionUser(req, resp, "ta", "mo", "admin");
        if (current == null) return;
        Map<String, Object> body = readBodyAsMap(req);
        String page = asString(body, "page");
        String message = asString(body, "message");
        AiConversation conversation = registry.aiConversationService().getOrCreateConversation(
            current.userId,
            current.role,
            page,
            asString(body, "sessionId")
        );
        registry.aiConversationService().appendMessage(conversation.sessionId, "user", message, null, List.of(), Map.of("page", page));
        Map<String, Object> data = registry.aiAssistantService().chat(current.userId, current.role, page, message);
        registry.aiConversationService().appendMessage(
            conversation.sessionId,
            "assistant",
            String.valueOf(data.getOrDefault("answer", "")),
            objectMap(data.get("answerView")),
            objectList(data.get("suggestedActions")),
            Map.of("modelCalled", data.getOrDefault("modelCalled", false), "provider", data.getOrDefault("provider", Map.of()))
        );
        data.put("sessionId", conversation.sessionId);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleMoCandidateSummary(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        User current = requireSessionUser(req, resp, "mo");
        if (current == null) return;
        Map<String, Object> body = readBodyAsMap(req);
        String applicationId = asString(body, "applicationId");
        AiConversation conversation = registry.aiConversationService().getOrCreateConversation(
            current.userId,
            current.role,
            firstNonBlank(asString(body, "page"), "mo/review"),
            asString(body, "sessionId")
        );
        Map<String, Object> data = registry.aiAssistantService().summarizeCandidateForMo(current.userId, applicationId);
        persistAssistantOutput(conversation.sessionId, "MO_CANDIDATE_SUMMARY", "mo/review", applicationId, data);
        data.put("sessionId", conversation.sessionId);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleAdminRiskAnalysis(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        User current = requireSessionUser(req, resp, "admin");
        if (current == null) return;
        Map<String, Object> body = readBodyAsMap(req);
        String riskLevel = asString(body, "riskLevel");
        AiConversation conversation = registry.aiConversationService().getOrCreateConversation(
            current.userId,
            current.role,
            firstNonBlank(asString(body, "page"), "admin/workload"),
            asString(body, "sessionId")
        );
        Map<String, Object> data = registry.aiAssistantService().analyzeAdminRisk(riskLevel);
        persistAssistantOutput(conversation.sessionId, "ADMIN_RISK_ANALYSIS", "admin/workload", riskLevel, data);
        data.put("sessionId", conversation.sessionId);
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private void handleActionExecute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        User current = requireSessionUser(req, resp, "ta", "mo", "admin");
        if (current == null) return;
        Map<String, Object> body = readBodyAsMap(req);
        String type = asString(body, "type");
        Map<String, Object> payload = objectMap(body.get("payload"));
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", type);

        switch (type) {
            case "TA_APPLY_JOB" -> {
                if (!"ta".equalsIgnoreCase(current.role)) {
                    throw new ServiceException(HttpServletResponse.SC_FORBIDDEN, "AUTH_FORBIDDEN_ROLE", "Only TA users can apply for jobs.");
                }
                Application application = registry.applicationService().createApplication(current.userId, value(payload, "jobId"));
                data.put("application", application);
                data.put("message", "Application submitted for " + application.jobId + ".");
            }
            case "MO_SELECT_APPLICATION", "MO_REJECT_APPLICATION" -> {
                if (!"mo".equalsIgnoreCase(current.role)) {
                    throw new ServiceException(HttpServletResponse.SC_FORBIDDEN, "AUTH_FORBIDDEN_ROLE", "Only MO users can review applications.");
                }
                String status = "MO_SELECT_APPLICATION".equals(type) ? "selected" : "rejected";
                Application application = registry.moService().updateApplicationStatus(
                    current.userId,
                    value(payload, "applicationId"),
                    status,
                    value(payload, "reviewNote")
                );
                data.put("application", application);
                data.put("message", "Application " + application.applicationId + " marked as " + application.status + ".");
            }
            case "FILTER_ADMIN_RISK" -> {
                if (!"admin".equalsIgnoreCase(current.role)) {
                    throw new ServiceException(HttpServletResponse.SC_FORBIDDEN, "AUTH_FORBIDDEN_ROLE", "Only Admin users can analyze workload risk.");
                }
                data.put("analysis", registry.aiAssistantService().analyzeAdminRisk(value(payload, "riskLevel")));
                data.put("message", "Risk analysis refreshed.");
            }
            case "NAVIGATE", "OPEN_CV" -> data.put("message", "Navigation action prepared.");
            default -> throw new ServiceException(422, "AI_ACTION_UNSUPPORTED", "Unsupported AI action.");
        }

        String sessionId = asString(body, "sessionId");
        if (!sessionId.isBlank()) {
            registry.aiConversationService().getConversation(current.userId, sessionId);
            registry.aiConversationService().appendMessage(
                sessionId,
                "system",
                String.valueOf(data.getOrDefault("message", "Action completed.")),
                null,
                List.of(),
                Map.of("actionType", type)
            );
        }
        JsonResponse.writeSuccess(resp, HttpServletResponse.SC_OK, data, null);
    }

    private String normalizePath(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null || path.isBlank()) {
            return "/";
        }
        return path;
    }

    private void persistAssistantOutput(
        String sessionId,
        String type,
        String sourcePage,
        String scopeKey,
        Map<String, Object> data
    ) throws IOException, ServiceException {
        registry.aiConversationService().saveArtifact(sessionId, type, sourcePage, scopeKey, data);
        registry.aiConversationService().appendMessage(
            sessionId,
            "assistant",
            String.valueOf(data.getOrDefault("summary", data.getOrDefault("guidance", "AI output generated."))),
            objectMap(data.get("modelView")),
            objectList(data.get("suggestedActions")),
            Map.of("artifactType", type, "modelCalled", data.getOrDefault("modelCalled", false))
        );
    }

    private Map<String, Object> objectMap(Object value) {
        if (value instanceof Map<?, ?> raw) {
            Map<String, Object> result = new LinkedHashMap<>();
            raw.forEach((key, item) -> result.put(String.valueOf(key), item));
            return result;
        }
        return Map.of();
    }

    private List<Map<String, Object>> objectList(Object value) {
        if (!(value instanceof List<?> list)) {
            return List.of();
        }
        return list.stream()
            .map(this::objectMap)
            .filter(item -> !item.isEmpty())
            .toList();
    }

    private String value(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        return value == null ? "" : String.valueOf(value).trim();
    }

    private String firstNonBlank(String first, String fallback) {
        return first == null || first.isBlank() ? fallback : first;
    }
}
