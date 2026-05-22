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
        /** Unique message id within the conversation. */
        public String messageId;
        /** Message role, such as {@code user}, {@code assistant}, or {@code system}. */
        public String role;
        /** Plain text message content shown in the chat history. */
        public String content;
        /** Creation timestamp for the message. */
        public String createdAt;
        /** Structured assistant view generated for rich UI rendering. */
        public Map<String, Object> modelView;
        /** Suggested UI actions derived from the message context. */
        public List<Map<String, Object>> suggestedActions = new ArrayList<>();
        /** Additional provider or workflow metadata for the message. */
        public Map<String, Object> metadata;
    }

    /**
     * Reusable structured AI output cached for later display.
     */
    public static class AiArtifact {
        /** Unique artifact id within the conversation. */
        public String artifactId;
        /** Artifact category, such as recommendation, summary, or risk analysis. */
        public String type;
        /** Page that produced or owns the artifact. */
        public String sourcePage;
        /** Optional key used to scope the artifact to a job, application, or user. */
        public String scopeKey;
        /** Creation timestamp for the artifact. */
        public String createdAt;
        /** Structured artifact payload rendered by the frontend. */
        public Map<String, Object> payload;
    }
}
