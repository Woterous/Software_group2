package com.group02.tars.service;

import com.group02.tars.entity.AiConversation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Stores AI assistant conversations, messages, and generated artifacts.
 */
public interface AiConversationService {
    /**
     * Lists recent conversations owned by one user.
     *
     * @param userId owner user id
     * @return recent conversations without cross-user data
     * @throws IOException if persisted data cannot be read
     */
    List<AiConversation> listConversations(String userId) throws IOException;

    /**
     * Loads one conversation owned by the current user.
     *
     * @param userId owner user id
     * @param sessionId conversation id
     * @return conversation
     * @throws IOException if persisted data cannot be read
     * @throws ServiceException if the conversation is missing or forbidden
     */
    AiConversation getConversation(String userId, String sessionId) throws IOException, ServiceException;

    /**
     * Gets an existing conversation or creates a fresh one.
     *
     * @param userId owner user id
     * @param role owner role
     * @param page page context
     * @param sessionId optional existing session id
     * @return conversation
     * @throws IOException if persisted data cannot be read or written
     * @throws ServiceException if an existing session is not accessible
     */
    AiConversation getOrCreateConversation(String userId, String role, String page, String sessionId)
        throws IOException, ServiceException;

    /**
     * Appends a message to a conversation.
     *
     * @param sessionId conversation id
     * @param role message role
     * @param content message text
     * @param modelView optional structured view
     * @param suggestedActions optional executable actions
     * @param metadata optional metadata
     * @return saved message
     * @throws IOException if persisted data cannot be read or written
     * @throws ServiceException if the conversation is missing
     */
    AiConversation.AiMessage appendMessage(
        String sessionId,
        String role,
        String content,
        Map<String, Object> modelView,
        List<Map<String, Object>> suggestedActions,
        Map<String, Object> metadata
    ) throws IOException, ServiceException;

    /**
     * Saves a generated AI artifact on the conversation.
     *
     * @param sessionId conversation id
     * @param type artifact type
     * @param sourcePage source page
     * @param scopeKey optional scope, such as job id or application id
     * @param payload generated data
     * @return saved artifact
     * @throws IOException if persisted data cannot be read or written
     * @throws ServiceException if the conversation is missing
     */
    AiConversation.AiArtifact saveArtifact(
        String sessionId,
        String type,
        String sourcePage,
        String scopeKey,
        Map<String, Object> payload
    ) throws IOException, ServiceException;

    /**
     * Returns the latest matching artifact for a user.
     *
     * @param userId owner user id
     * @param type artifact type
     * @param scopeKey optional scope filter
     * @return matching artifact or {@code null}
     * @throws IOException if persisted data cannot be read
     */
    AiConversation.AiArtifact latestArtifact(String userId, String type, String scopeKey) throws IOException;
}
