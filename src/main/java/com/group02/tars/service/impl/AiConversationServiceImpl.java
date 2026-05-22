package com.group02.tars.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.group02.tars.entity.AiConversation;
import com.group02.tars.service.AiConversationService;
import com.group02.tars.service.ServiceException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * JSON-file implementation for persisted assistant conversations.
 */
public class AiConversationServiceImpl implements AiConversationService {
    private static final TypeReference<List<AiConversation>> CONVERSATION_LIST_TYPE = new TypeReference<>() {};
    private static final int MAX_CONVERSATIONS_PER_USER = 12;
    private static final int MAX_MESSAGES_PER_CONVERSATION = 80;

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final Path conversationsFile;
    private final Object lock = new Object();

    /**
     * Creates the service using the active data directory.
     *
     * @param dataDir resolved project data directory
     * @throws IOException if the file cannot be initialized
     */
    public AiConversationServiceImpl(Path dataDir) throws IOException {
        Objects.requireNonNull(dataDir);
        Files.createDirectories(dataDir);
        this.conversationsFile = dataDir.resolve("ai_conversations.json").normalize();
        if (!Files.exists(conversationsFile)) {
            mapper.writeValue(conversationsFile.toFile(), List.of());
        }
    }

    @Override
    public List<AiConversation> listConversations(String userId) throws IOException {
        String owner = normalize(userId);
        synchronized (lock) {
            return loadAll().stream()
                .filter(conversation -> owner.equals(normalize(conversation.userId)))
                .sorted(Comparator.comparing((AiConversation c) -> normalize(c.updatedAt)).reversed())
                .limit(MAX_CONVERSATIONS_PER_USER)
                .map(this::copyConversation)
                .toList();
        }
    }

    @Override
    public AiConversation getConversation(String userId, String sessionId) throws IOException, ServiceException {
        String owner = normalize(userId);
        String id = normalize(sessionId);
        synchronized (lock) {
            AiConversation conversation = findById(loadAll(), id);
            if (!owner.equals(normalize(conversation.userId))) {
                throw new ServiceException(HttpServletResponse.SC_FORBIDDEN, "AI_SESSION_FORBIDDEN", "AI conversation is not accessible.");
            }
            return copyConversation(conversation);
        }
    }

    @Override
    public AiConversation getOrCreateConversation(String userId, String role, String page, String sessionId)
        throws IOException, ServiceException {
        String owner = normalize(userId);
        String id = normalize(sessionId);
        synchronized (lock) {
            List<AiConversation> conversations = loadAll();
            if (!id.isBlank()) {
                AiConversation existing = findById(conversations, id);
                if (!owner.equals(normalize(existing.userId))) {
                    throw new ServiceException(HttpServletResponse.SC_FORBIDDEN, "AI_SESSION_FORBIDDEN", "AI conversation is not accessible.");
                }
                existing.role = normalize(role);
                existing.page = normalize(page);
                existing.updatedAt = now();
                saveAll(prune(conversations));
                return copyConversation(existing);
            }

            AiConversation created = new AiConversation();
            created.sessionId = nextId("AIS", conversations.stream().map(c -> c.sessionId).toList());
            created.userId = owner;
            created.role = normalize(role);
            created.page = normalize(page);
            created.title = titleFor(created.role, created.page);
            created.createdAt = now();
            created.updatedAt = created.createdAt;
            conversations.add(created);
            saveAll(prune(conversations));
            return copyConversation(created);
        }
    }

    @Override
    public AiConversation.AiMessage appendMessage(
        String sessionId,
        String role,
        String content,
        Map<String, Object> modelView,
        List<Map<String, Object>> suggestedActions,
        Map<String, Object> metadata
    ) throws IOException, ServiceException {
        synchronized (lock) {
            List<AiConversation> conversations = loadAll();
            AiConversation conversation = findById(conversations, normalize(sessionId));
            AiConversation.AiMessage message = new AiConversation.AiMessage();
            message.messageId = nextId("MSG", conversation.messages.stream().map(m -> m.messageId).toList());
            message.role = normalize(role);
            message.content = normalize(content);
            message.createdAt = now();
            message.modelView = modelView == null ? null : new LinkedHashMap<>(modelView);
            message.suggestedActions = suggestedActions == null ? new ArrayList<>() : new ArrayList<>(suggestedActions);
            message.metadata = metadata == null ? null : new LinkedHashMap<>(metadata);

            conversation.messages.add(message);
            if (conversation.messages.size() > MAX_MESSAGES_PER_CONVERSATION) {
                conversation.messages = new ArrayList<>(conversation.messages.subList(
                    conversation.messages.size() - MAX_MESSAGES_PER_CONVERSATION,
                    conversation.messages.size()
                ));
            }
            conversation.updatedAt = message.createdAt;
            saveAll(prune(conversations));
            return message;
        }
    }

    @Override
    public AiConversation.AiArtifact saveArtifact(
        String sessionId,
        String type,
        String sourcePage,
        String scopeKey,
        Map<String, Object> payload
    ) throws IOException, ServiceException {
        synchronized (lock) {
            List<AiConversation> conversations = loadAll();
            AiConversation conversation = findById(conversations, normalize(sessionId));
            AiConversation.AiArtifact artifact = new AiConversation.AiArtifact();
            artifact.artifactId = nextId("AIA", conversation.artifacts.stream().map(a -> a.artifactId).toList());
            artifact.type = normalize(type);
            artifact.sourcePage = normalize(sourcePage);
            artifact.scopeKey = normalize(scopeKey);
            artifact.createdAt = now();
            artifact.payload = payload == null ? new LinkedHashMap<>() : new LinkedHashMap<>(payload);
            conversation.artifacts.add(artifact);
            conversation.updatedAt = artifact.createdAt;
            saveAll(prune(conversations));
            return artifact;
        }
    }

    @Override
    public AiConversation.AiArtifact latestArtifact(String userId, String type, String scopeKey) throws IOException {
        String owner = normalize(userId);
        String artifactType = normalize(type);
        String scope = normalize(scopeKey);
        synchronized (lock) {
            return loadAll().stream()
                .filter(conversation -> owner.equals(normalize(conversation.userId)))
                .flatMap(conversation -> conversation.artifacts == null ? List.<AiConversation.AiArtifact>of().stream() : conversation.artifacts.stream())
                .filter(artifact -> artifactType.isBlank() || artifactType.equalsIgnoreCase(normalize(artifact.type)))
                .filter(artifact -> scope.isBlank() || scope.equalsIgnoreCase(normalize(artifact.scopeKey)))
                .max(Comparator.comparing(artifact -> normalize(artifact.createdAt)))
                .orElse(null);
        }
    }

    private List<AiConversation> loadAll() throws IOException {
        if (!Files.exists(conversationsFile) || Files.size(conversationsFile) == 0L) {
            return new ArrayList<>();
        }
        return new ArrayList<>(mapper.readValue(conversationsFile.toFile(), CONVERSATION_LIST_TYPE));
    }

    private void saveAll(List<AiConversation> conversations) throws IOException {
        mapper.writeValue(conversationsFile.toFile(), conversations);
    }

    private AiConversation findById(List<AiConversation> conversations, String sessionId) throws ServiceException {
        return conversations.stream()
            .filter(conversation -> normalize(sessionId).equals(normalize(conversation.sessionId)))
            .findFirst()
            .orElseThrow(() -> new ServiceException(HttpServletResponse.SC_NOT_FOUND, "AI_SESSION_NOT_FOUND", "AI conversation not found."));
    }

    private List<AiConversation> prune(List<AiConversation> conversations) {
        Map<String, List<AiConversation>> byUser = new LinkedHashMap<>();
        for (AiConversation conversation : conversations) {
            byUser.computeIfAbsent(normalize(conversation.userId), key -> new ArrayList<>()).add(conversation);
        }
        List<AiConversation> pruned = new ArrayList<>();
        for (List<AiConversation> userConversations : byUser.values()) {
            userConversations.sort(Comparator.comparing((AiConversation c) -> normalize(c.updatedAt)).reversed());
            pruned.addAll(userConversations.stream().limit(MAX_CONVERSATIONS_PER_USER).toList());
        }
        return pruned;
    }

    private AiConversation copyConversation(AiConversation source) {
        AiConversation copy = new AiConversation();
        copy.sessionId = source.sessionId;
        copy.userId = source.userId;
        copy.role = source.role;
        copy.page = source.page;
        copy.title = source.title;
        copy.createdAt = source.createdAt;
        copy.updatedAt = source.updatedAt;
        copy.messages = source.messages == null ? new ArrayList<>() : new ArrayList<>(source.messages);
        copy.artifacts = source.artifacts == null ? new ArrayList<>() : new ArrayList<>(source.artifacts);
        return copy;
    }

    private String titleFor(String role, String page) {
        String normalizedPage = normalize(page);
        if (normalizedPage.contains("review")) {
            return "Candidate review assistant";
        }
        if (normalizedPage.contains("jobs")) {
            return "Job recommendation assistant";
        }
        if (normalizedPage.contains("workload")) {
            return "Workload risk assistant";
        }
        return normalize(role).toUpperCase() + " workspace assistant";
    }

    private String nextId(String prefix, List<String> existingIds) {
        int max = 0;
        for (String id : existingIds) {
            String normalized = normalize(id);
            if (normalized.startsWith(prefix)) {
                try {
                    max = Math.max(max, Integer.parseInt(normalized.substring(prefix.length())));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return prefix + String.format("%03d", max + 1);
    }

    private String now() {
        return LocalDateTime.now().withNano(0).toString();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
