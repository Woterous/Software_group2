(function () {
    function byId(id) {
        return document.getElementById(id);
    }

    function escapeHtml(input) {
        const div = document.createElement("div");
        div.textContent = input == null ? "" : String(input);
        return div.innerHTML;
    }

    function toast(message, level = "info", timeout = 2600) {
        const root = byId("toast-root");
        if (!root) return;
        const node = document.createElement("div");
        node.className = `toast ${level}`;
        node.innerHTML = `<strong>${level.toUpperCase()}</strong><div>${escapeHtml(message)}</div>`;
        root.appendChild(node);
        setTimeout(() => {
            node.style.opacity = "0";
            node.style.transform = "translateX(20px)";
            setTimeout(() => node.remove(), 180);
        }, timeout);
    }

    function setSelectOptions(selectEl, items, valueKey = "value", labelKey = "label", includeAll = false, allLabel = "All") {
        if (!selectEl) return;
        const current = selectEl.value;
        let html = includeAll ? `<option value="">${allLabel}</option>` : "";
        html += items.map((item) => {
            const v = typeof item === "string" ? item : item[valueKey];
            const l = typeof item === "string" ? item : item[labelKey];
            return `<option value="${escapeHtml(v)}">${escapeHtml(l)}</option>`;
        }).join("");
        selectEl.innerHTML = html;
        if ([...selectEl.options].some((o) => o.value === current)) {
            selectEl.value = current;
        }
    }

    function formToObject(form) {
        const fd = new FormData(form);
        return Object.fromEntries(fd.entries());
    }

    function getQuery(name) {
        return new URLSearchParams(window.location.search).get(name);
    }

    function badge(status) {
        const safe = (status || "").toLowerCase();
        return `<span class="badge ${safe}">${escapeHtml(status || "-")}</span>`;
    }

    function formatDate(date) {
        return date || "-";
    }

    function renderPagination(container, meta, onChange) {
        if (!container || !meta) return;
        const chips = [];
        const total = meta.totalPages || 1;
        for (let p = 1; p <= total; p += 1) {
            chips.push(`<button class="page-chip ${p === meta.page ? "active" : ""}" data-page="${p}">${p}</button>`);
        }
        container.innerHTML = chips.join("");
        container.querySelectorAll(".page-chip").forEach((btn) => {
            btn.addEventListener("click", () => onChange(Number(btn.dataset.page)));
        });
    }

    function roleHome(role) {
        if (role === "mo") return `${window.APP_CONTEXT}/pages/mo/dashboard`;
        if (role === "admin") return `${window.APP_CONTEXT}/pages/admin/dashboard`;
        return `${window.APP_CONTEXT}/pages/ta/dashboard`;
    }

    function openModal({ title, message, onConfirm }) {
        const root = byId("modal-root");
        if (!root) return;
        byId("modal-title").textContent = title || "Confirm";
        byId("modal-message").textContent = message || "Please confirm this action.";
        root.classList.remove("hidden");

        const close = () => {
            root.classList.add("hidden");
            confirmBtn.removeEventListener("click", confirmHandler);
            cancelBtn.removeEventListener("click", close);
            backdrop.removeEventListener("click", close);
        };

        const confirmHandler = () => {
            close();
            if (typeof onConfirm === "function") onConfirm();
        };

        const confirmBtn = root.querySelector('[data-action="modal-confirm"]');
        const cancelBtn = root.querySelector('[data-action="modal-cancel"]');
        const backdrop = root.querySelector('[data-action="modal-close"]');

        confirmBtn.addEventListener("click", confirmHandler);
        cancelBtn.addEventListener("click", close);
        backdrop.addEventListener("click", close);
    }

    function bindGlobalActions() {
        document.querySelectorAll('[data-action="theme-toggle"]').forEach((btn) => {
            btn.addEventListener("click", () => {
                document.body.classList.toggle("theme-soft");
            });
        });

        document.querySelectorAll('[data-action="mock-reset"]').forEach((btn) => {
            btn.addEventListener("click", () => {
                window.MockEngine.reset();
                toast("Mock data reset to initial seed.", "success");
                setTimeout(() => window.location.reload(), 500);
            });
        });
    }

    function ensureSessionOrRedirect(allowedRoles) {
        const session = window.MockEngine.getSession();
        if (!session || (allowedRoles && !allowedRoles.includes(session.role))) {
            window.location.href = `${window.APP_CONTEXT}/pages/login`;
            return null;
        }
        return session;
    }

    window.UIKit = {
        byId,
        toast,
        badge,
        formToObject,
        getQuery,
        formatDate,
        setSelectOptions,
        renderPagination,
        roleHome,
        openModal,
        bindGlobalActions,
        ensureSessionOrRedirect,
        escapeHtml
    };
})();
