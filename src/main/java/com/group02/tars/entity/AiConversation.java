package com.group02.tars.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Persisted AI assistant conversation with messages and reusable artifacts.
 */
public class AiConversation {
    /** Unique conversation id, such as {@code AIS001}. */
    public String sessionId;
    /** User that owns this conversation. */
    public String userId;
    /** Role snapshot for the owner. */
    public String role;
    /** Last page context associated with the conversation. */
    public String page;
    /** Short title shown in the assistant history. */
    public String title;
    /** Creation timestamp. */
    public String createdAt;
    /** Last update timestamp. */
    public String updatedAt;
    /** Ordered chat messages. */
    public List<AiMessage> messages = new ArrayList<>();
    /** Stored AI outputs such as recommendations, summaries, and risk analysis. */
    public List<AiArtifact> artifacts = new ArrayList<>();

    /**
     * One user, assistant, or system action message.
     */
    public static class AiMessage {
        public String messageId;
        public String role;
        public String content;
        public String createdAt;
        public Map<String, Object> modelView;
        public List<Map<String, Object>> suggestedActions = new ArrayList<>();
        public Map<String, Object> metadata;
    }

    /**
     * Reusable structured AI output cached for later display.
     */
    public static class AiArtifact {
        public String artifactId;
        public String type;
        public String sourcePage;
        public String scopeKey;
        public String createdAt;
        public Map<String, Object> payload;
    }
}
