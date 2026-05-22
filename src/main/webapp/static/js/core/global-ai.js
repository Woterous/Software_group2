/**
 * Persistent AI assistant drawer with role-aware chat and executable actions.
 */
(function () {
    const LEGACY_SESSION_KEY = "tars.ai.sessionId";

    function currentUser() {
        try {
            return JSON.parse(window.sessionStorage.getItem("tars.session.user") || "null") || {};
        } catch (_) {
            return {};
        }
    }

    function sessionKey() {
        const user = currentUser();
        const role = user.role || document.body.dataset.role || "public";
        const userId = user.userId || "anonymous";
        return `tars.ai.sessionId.${role}.${userId}`;
    }

    window.sessionStorage.removeItem(LEGACY_SESSION_KEY);
    let activeSessionId = window.sessionStorage.getItem(sessionKey()) || "";

    function clearActiveAiSession() {
        activeSessionId = "";
        window.sessionStorage.removeItem(sessionKey());
    }

    function isAuthError(result) {
        return result?.error?.code === "AUTH_NOT_LOGIN" || result?.error?.code === "HTTP_401";
    }

    function handleAuthError(messagesEl) {
        clearActiveAiSession();
        if (messagesEl) {
            appendMessage(messagesEl, "assistant error", "Your session has expired. Please log in again.");
        }
        if (typeof window.UIKit?.handleSessionExpired === "function") {
            window.UIKit.handleSessionExpired("Session expired. Please log in again.");
        }
    }

    function pageContext() {
        return document.body.dataset.page || window.location.pathname;
    }

    function actionButtonClass(action) {
        const tone = action?.tone || "secondary";
        if (tone === "primary") return "primary-btn ai-action-btn";
        if (tone === "danger") return "danger-btn ai-action-btn";
        return "glass-secondary-btn ai-action-btn";
    }

    function renderActions(actions = []) {
        const rows = Array.isArray(actions) ? actions : [];
        if (!rows.length) return "";
        return `
            <div class="global-ai-actions">
                ${rows.map((action, index) => `
                    <button class="${actionButtonClass(action)}" type="button" data-ai-action-index="${index}">
                        ${window.UIKit.escapeHtml(action.label || action.type || "Action")}
                    </button>
                `).join("")}
            </div>
        `;
    }

    function normalizeTextList(value) {
        if (!Array.isArray(value)) return [];
        return value.map((item) => String(item || "").trim()).filter(Boolean);
    }

    function normalizeObjectList(value) {
        return Array.isArray(value) ? value.filter((item) => item && typeof item === "object") : [];
    }

    function renderInlineList(items = []) {
        const rows = normalizeTextList(items);
        if (!rows.length) return "";
        return `<div class="global-ai-card-badges">${rows.map((item) => `<span>${window.UIKit.escapeHtml(item)}</span>`).join("")}</div>`;
    }

    function renderMetricGrid(metrics = []) {
        const rows = normalizeObjectList(metrics);
        if (!rows.length) return "";
        return `
            <div class="global-ai-card-metrics">
                ${rows.map((metric) => `
                    <div>
                        <span>${window.UIKit.escapeHtml(metric.label || "")}</span>
                        <strong>${window.UIKit.escapeHtml(metric.value || "")}</strong>
                    </div>
                `).join("")}
            </div>
        `;
    }

    function renderCardActions(actions = [], offset = 0) {
        const rows = normalizeObjectList(actions);
        if (!rows.length) return "";
        return `
            <div class="global-ai-card-actions">
                ${rows.map((action, index) => `
                    <button class="${actionButtonClass(action)}" type="button" data-ai-action-index="${offset + index}">
                        ${window.UIKit.escapeHtml(action.label || action.type || "Action")}
                    </button>
                `).join("")}
            </div>
        `;
    }

    function renderAssistantCards(cards = []) {
        const rows = normalizeObjectList(cards);
        let actionOffset = 0;
        const html = rows.map((card) => {
            const actions = normalizeObjectList(card.actions);
            const type = String(card.type || "info").replace(/[^a-z0-9_-]/gi, "").toLowerCase() || "info";
            const actionHtml = renderCardActions(actions, actionOffset);
            actionOffset += actions.length;
            return `
                <article class="global-ai-result-card global-ai-result-card--${type}">
                    <div class="global-ai-result-card-head">
                        <div>
                            ${card.kicker ? `<span class="section-kicker">${window.UIKit.escapeHtml(card.kicker)}</span>` : ""}
                            <h4>${window.UIKit.escapeHtml(card.title || "Recommendation")}</h4>
                            ${card.subtitle ? `<p>${window.UIKit.escapeHtml(card.subtitle)}</p>` : ""}
                        </div>
                        ${card.score ? `<strong class="global-ai-score">${window.UIKit.escapeHtml(card.score)}</strong>` : ""}
                    </div>
                    ${renderInlineList(card.badges || [])}
                    ${renderMetricGrid(card.metrics || [])}
                    ${card.body ? `<p class="global-ai-card-copy">${window.UIKit.escapeHtml(card.body)}</p>` : ""}
                    ${renderChatSections(card.sections || [])}
                    ${actionHtml}
                </article>
            `;
        }).join("");
        return { html: html ? `<div class="global-ai-result-cards">${html}</div>` : "", actions: rows.flatMap((card) => normalizeObjectList(card.actions)) };
    }

    function renderChatSections(sections = []) {
        const rows = Array.isArray(sections) ? sections : [];
        const html = rows.map((section) => {
            const items = normalizeTextList(section.items);
            if (!items.length) return "";
            const tone = String(section.tone || "action").replace(/[^a-z0-9_-]/gi, "").toLowerCase() || "action";
            return `
                <section class="global-ai-card-section global-ai-card-section--${tone}">
                    <h4>${window.UIKit.escapeHtml(section.title || "Details")}</h4>
                    <ul>
                        ${items.map((item) => `<li>${window.UIKit.escapeHtml(item)}</li>`).join("")}
                    </ul>
                </section>
            `;
        }).join("");
        return html ? `<div class="global-ai-card-sections">${html}</div>` : "";
    }

    function renderChatStructuredView(data) {
        const view = data?.answerView || {};
        const answer = view.answer || data?.answer || "";
        const cards = renderAssistantCards(view.cards || []);
        return `
            <article class="global-ai-card global-ai-card--chat">
                ${answer ? `<p class="global-ai-chat-copy">${window.UIKit.escapeHtml(answer)}</p>` : ""}
                ${cards.html}
                ${renderChatSections(view.sections || [])}
            </article>
        `;
    }

    function appendMessage(root, role, content, meta, actions = []) {
        if (!root) return null;
        const node = document.createElement("div");
        node.className = `global-ai-message ${role}`;
        node.dataset.actions = JSON.stringify(actions || []);
        node.innerHTML = `
            <div class="global-ai-message-body">${window.UIKit.escapeHtml(content || "")}</div>
            ${renderActions(actions)}
            ${meta ? `<small>${window.UIKit.escapeHtml(meta)}</small>` : ""}
        `;
        root.appendChild(node);
        scrollMessagesToBottom(root);
        bindActionButtons(node);
        return node;
    }

    function appendStructuredMessage(root, data) {
        if (!root || !data?.answerView) {
            return appendMessage(root, "assistant", data?.answer || "No answer returned.", "", data?.suggestedActions || []);
        }
        const cards = normalizeObjectList(data.answerView.cards);
        const cardActions = cards.flatMap((card) => normalizeObjectList(card.actions));
        const actions = cardActions.length ? cardActions : normalizeObjectList(data.suggestedActions);
        const node = document.createElement("div");
        node.className = "global-ai-message assistant global-ai-message--structured";
        node.dataset.actions = JSON.stringify(actions);
        node.innerHTML = `
            ${renderChatStructuredView(data)}
            ${cardActions.length ? "" : renderActions(actions)}
        `;
        root.appendChild(node);
        scrollMessagesToBottom(root);
        bindActionButtons(node);
        return node;
    }

    function scrollMessagesToBottom(messagesEl) {
        if (!messagesEl) return;
        window.requestAnimationFrame(() => {
            messagesEl.scrollTop = messagesEl.scrollHeight;
        });
    }

    function compareMessages(a, b) {
        const timeA = Date.parse(a?.createdAt || "") || 0;
        const timeB = Date.parse(b?.createdAt || "") || 0;
        if (timeA !== timeB) return timeA - timeB;
        return String(a?.messageId || "").localeCompare(String(b?.messageId || ""));
    }

    function renderPersistedMessages(messagesEl, conversation) {
        messagesEl.innerHTML = "";
        const messages = [...(conversation?.messages || [])].sort(compareMessages);
        if (!messages.length) {
            appendMessage(messagesEl, "assistant", "Ask about this recruitment workspace, current role workflow, workload risks, candidate review, or what to do next.");
            scrollMessagesToBottom(messagesEl);
            return;
        }
        messages.forEach((message) => {
            if (message.modelView) {
                appendStructuredMessage(messagesEl, {
                    answer: message.content,
                    answerView: message.modelView,
                    suggestedActions: message.suggestedActions || [],
                    modelCalled: message.metadata?.modelCalled,
                    provider: message.metadata?.provider || {}
                });
            } else {
                appendMessage(messagesEl, message.role || "assistant", message.content || "", message.createdAt || "", message.suggestedActions || []);
            }
        });
        scrollMessagesToBottom(messagesEl);
    }

    async function loadConversations(messagesEl, listEl) {
        const result = await window.ApiClient.aiConversations();
        if (!result.success) {
            if (isAuthError(result)) handleAuthError(messagesEl);
            return;
        }
        const conversations = result.data.conversations || [];
        if (listEl) {
            listEl.innerHTML = conversations.map((item) => `
                <button class="global-ai-history-item ${item.sessionId === activeSessionId ? "active" : ""}" type="button" data-session-id="${window.UIKit.escapeHtml(item.sessionId)}">
                    <strong>${window.UIKit.escapeHtml(item.title || "AI conversation")}</strong>
                    <span>${window.UIKit.escapeHtml(item.updatedAt || "")}</span>
                </button>
            `).join("");
            listEl.querySelectorAll("[data-session-id]").forEach((btn) => {
                btn.addEventListener("click", async () => {
                    activeSessionId = btn.dataset.sessionId;
                    window.sessionStorage.setItem(sessionKey(), activeSessionId);
                    await loadActiveConversation(messagesEl);
                    await loadConversations(messagesEl, listEl);
                });
            });
        }
        if (!activeSessionId && conversations[0]?.sessionId) {
            activeSessionId = conversations[0].sessionId;
            window.sessionStorage.setItem(sessionKey(), activeSessionId);
        }
        if (activeSessionId) {
            await loadActiveConversation(messagesEl);
        }
    }

    async function loadActiveConversation(messagesEl) {
        if (!activeSessionId || !messagesEl) return;
        const result = await window.ApiClient.aiConversation(activeSessionId);
        if (result.success) {
            renderPersistedMessages(messagesEl, result.data.conversation);
            return;
        }
        if (isAuthError(result)) {
            handleAuthError(messagesEl);
            return;
        }
        if (result.error?.code === "AI_SESSION_FORBIDDEN") {
            clearActiveAiSession();
            renderPersistedMessages(messagesEl, { messages: [] });
        }
    }

    function actionConfirmation(action) {
        const payload = action?.payload || {};
        const label = action?.label || action?.type || "Action";
        const type = action?.type || "";
        if (type === "MO_SELECT_APPLICATION") {
            return {
                title: "Select TA",
                message: `Confirm selecting application ${payload.applicationId || ""}? The dashboard and review data will refresh after the decision is saved.`,
                confirmText: "Select TA",
                tone: "primary"
            };
        }
        if (type === "MO_REJECT_APPLICATION") {
            return {
                title: "Reject Application",
                message: `Confirm rejecting application ${payload.applicationId || ""}? The dashboard and review data will refresh after the decision is saved.`,
                confirmText: "Reject",
                tone: "danger"
            };
        }
        if (type === "TA_APPLY_JOB") {
            return {
                title: "Submit Application",
                message: `Submit your application${payload.title ? ` for ${payload.title}` : ""}?`,
                confirmText: "Apply",
                tone: "primary"
            };
        }
        return {
            title: "Confirm AI Action",
            message: `Run action: ${label}?`,
            confirmText: label,
            tone: action?.tone === "danger" ? "danger" : "primary"
        };
    }

    async function confirmAction(action) {
        if (!action?.requiresConfirmation) return true;
        const options = actionConfirmation(action);
        if (typeof window.UIKit?.openModal === "function") {
            return window.UIKit.openModal(options);
        }
        return false;
    }

    function notifyActionCompleted(action, resultData) {
        document.dispatchEvent(new CustomEvent("tars:ai-action-completed", {
            detail: {
                type: action?.type || "",
                payload: action?.payload || {},
                result: resultData || {}
            }
        }));
    }

    async function executeAction(action, button) {
        const payload = action?.payload || {};
        if (action?.type === "NAVIGATE") {
            if (payload.url) window.UIKit.navigateWithTransition(`${window.APP_CONTEXT}${payload.url}`);
            return;
        }
        if (action?.type === "OPEN_CV") {
            await window.ApiClient.openCvFile(payload.cvPath);
            return;
        }
        if (!(await confirmAction(action))) {
            return;
        }
        button.disabled = true;
        let result;
        try {
            result = await window.ApiClient.aiExecuteAction({
                sessionId: activeSessionId,
                type: action.type,
                payload
            });
        } finally {
            button.disabled = false;
        }
        if (!result.success) {
            if (isAuthError(result)) {
                handleAuthError(document.getElementById("global-ai-messages"));
                return;
            }
            window.UIKit.toast(result.error?.message || "AI action failed.", "error");
            return;
        }
        notifyActionCompleted(action, result.data);
        window.UIKit.toast(result.data?.message || "Action completed.", "success");
        const messages = document.getElementById("global-ai-messages");
        if (messages) appendMessage(messages, "assistant", result.data?.message || "Action completed.", "Action result");
    }

    function bindActionButtons(scope) {
        scope.querySelectorAll("[data-ai-action-index]").forEach((btn) => {
            btn.addEventListener("click", async () => {
                const container = btn.closest(".global-ai-message, .ai-result-card, .ai-result-stack");
                const actions = parseActions(container?.dataset.actions || "[]");
                const action = actions[Number(btn.dataset.aiActionIndex)];
                if (action) await executeAction(action, btn);
            });
        });
    }

    function parseActions(raw) {
        try {
            return JSON.parse(raw || "[]");
        } catch (_) {
            try {
                return JSON.parse(decodeURIComponent(raw || "[]"));
            } catch (__) {
                return [];
            }
        }
    }

    function initGlobalAssistant() {
        const panel = document.getElementById("global-ai-assistant");
        const backdrop = document.querySelector(".global-ai-backdrop");
        const form = document.getElementById("global-ai-form");
        const messages = document.getElementById("global-ai-messages");
        const historyEl = document.getElementById("global-ai-history");
        const newBtn = document.getElementById("global-ai-new-chat");
        if (!panel || !backdrop || !form || !messages) return;
        const messageInput = form.message;
        let closeTimer = null;

        const resizeComposer = () => {
            if (!messageInput) return;
            messageInput.style.height = "auto";
            messageInput.style.height = `${Math.min(messageInput.scrollHeight, 132)}px`;
        };

        const setOpenState = (isOpen) => {
            panel.classList.toggle("is-open", isOpen);
            backdrop.classList.toggle("is-open", isOpen);
            panel.setAttribute("aria-hidden", String(!isOpen));
        };

        const open = async () => {
            if (closeTimer) {
                window.clearTimeout(closeTimer);
                closeTimer = null;
            }
            panel.classList.remove("hidden");
            backdrop.classList.remove("hidden");
            panel.classList.remove("is-closing");
            backdrop.classList.remove("is-closing");
            window.requestAnimationFrame(() => setOpenState(true));
            await loadConversations(messages, historyEl);
            resizeComposer();
            messageInput.focus();
        };

        const close = () => {
            if (panel.classList.contains("hidden")) return;
            setOpenState(false);
            panel.classList.add("is-closing");
            backdrop.classList.add("is-closing");
            closeTimer = window.setTimeout(() => {
                panel.classList.add("hidden");
                backdrop.classList.add("hidden");
                panel.classList.remove("is-closing");
                backdrop.classList.remove("is-closing");
                closeTimer = null;
            }, 260);
        };

        document.querySelectorAll('[data-action="ai-open"]').forEach((btn) => btn.addEventListener("click", open));
        document.querySelectorAll('[data-action="ai-close"]').forEach((btn) => btn.addEventListener("click", close));
        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape" && panel.classList.contains("is-open")) close();
        });
        newBtn?.addEventListener("click", () => {
            activeSessionId = "";
            window.sessionStorage.removeItem(sessionKey());
            messages.innerHTML = "";
            appendMessage(messages, "assistant", "New conversation started. Ask a question or request an action.");
            form.reset();
            resizeComposer();
            messageInput.focus();
        });
        messageInput?.addEventListener("input", resizeComposer);
        resizeComposer();

        form.addEventListener("submit", async (event) => {
            event.preventDefault();
            const message = messageInput.value.trim();
            if (!message) return;
            appendMessage(messages, "user", message);
            form.reset();
            resizeComposer();

            const submitBtn = form.querySelector("button[type='submit']");
            submitBtn.disabled = true;
            const loading = appendMessage(messages, "assistant is-loading", "Analyzing workspace context...");
            const result = await window.ApiClient.aiChat({
                sessionId: activeSessionId,
                message,
                page: pageContext()
            });
            submitBtn.disabled = false;
            loading?.remove();

            if (!result.success) {
                if (isAuthError(result)) {
                    handleAuthError(messages);
                    return;
                }
                if (result.error?.code === "AI_SESSION_FORBIDDEN") {
                    clearActiveAiSession();
                }
                appendMessage(messages, "assistant error", result.error?.message || "AI request failed.");
                return;
            }

            const data = result.data || {};
            if (data.sessionId) {
                activeSessionId = data.sessionId;
                window.sessionStorage.setItem(sessionKey(), activeSessionId);
            }
            appendStructuredMessage(messages, data);
            await loadConversations(messages, historyEl);
        });
    }

    window.AiAssistant = {
        getSessionId: () => activeSessionId,
        setSessionId: (sessionId) => {
            activeSessionId = sessionId || "";
            if (activeSessionId) {
                window.sessionStorage.setItem(sessionKey(), activeSessionId);
            } else {
                window.sessionStorage.removeItem(sessionKey());
            }
        },
        renderActions,
        bindActionButtons
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initGlobalAssistant);
    } else {
        initGlobalAssistant();
    }
})();
