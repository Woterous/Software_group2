(function () {
    const cfg = window.TARS_CONFIG;

    const okEnvelope = (data, meta = null) => ({ success: true, data, meta, error: null });
    const errEnvelope = (code, message, details = []) => ({ success: false, data: null, meta: null, error: { code, message, details } });

    async function fetchJson(path, options = {}) {
        const res = await fetch(`${cfg.apiBasePath}${path}`, {
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
                ...(options.headers || {})
            },
            ...options
        });
        const payload = await res.json();
        return payload;
    }

    function fromMock(result, meta = null) {
        if (!result.ok) {
            return errEnvelope(result.error.code, result.error.message, result.error.details || []);
        }
        return okEnvelope(result.data, meta || result.meta || null);
    }

    window.ApiClient = {
        mode: cfg.dataSource,

        async authLogin(payload) {
            if (this.mode === "mock") return fromMock(window.MockEngine.auth.login(payload));
            return fetchJson("/auth/login", { method: "POST", body: JSON.stringify(payload) });
        },
        async authRegister(payload) {
            if (this.mode === "mock") return fromMock(window.MockEngine.auth.register(payload));
            return fetchJson("/auth/register", { method: "POST", body: JSON.stringify(payload) });
        },
        async authLogout() {
            if (this.mode === "mock") return fromMock(window.MockEngine.auth.logout());
            return fetchJson("/auth/logout", { method: "DELETE" });
        },
        async authMe() {
            if (this.mode === "mock") return fromMock(window.MockEngine.auth.me());
            return fetchJson("/auth/me", { method: "GET" });
        },

        async taDashboard(userId) {
            if (this.mode === "mock") return fromMock(window.MockEngine.ta.dashboard(userId));
            return fetchJson("/ta/dashboard", { method: "GET" });
        },
        async taProfile(userId) {
            if (this.mode === "mock") return fromMock(window.MockEngine.ta.getProfile(userId));
            return fetchJson("/ta/profile", { method: "GET" });
        },
        async taUpdateProfile(userId, payload) {
            if (this.mode === "mock") return fromMock(window.MockEngine.ta.updateProfile(userId, payload));
            return fetchJson("/ta/profile", { method: "PUT", body: JSON.stringify(payload) });
        },
        async taUpdateCv(userId, payload) {
            if (this.mode === "mock") return fromMock(window.MockEngine.ta.updateCv(userId, payload.cvPath));
            return fetchJson("/ta/profile/cv", { method: "POST", body: JSON.stringify(payload) });
        },
        async taDeleteCv(userId) {
            if (this.mode === "mock") return fromMock(window.MockEngine.ta.updateCv(userId, ""));
            return fetchJson("/ta/profile/cv", { method: "DELETE" });
        },
        async taListJobs(params) {
            if (this.mode === "mock") {
                const result = window.MockEngine.ta.listJobs(params);
                return fromMock(result, result.meta || null);
            }
            const query = new URLSearchParams(params).toString();
            return fetchJson(`/ta/jobs?${query}`, { method: "GET" });
        },
        async taJobDetail(jobId) {
            if (this.mode === "mock") return fromMock(window.MockEngine.ta.getJobDetail(jobId));
            return fetchJson(`/ta/jobs/${jobId}`, { method: "GET" });
        },
        async taApply(userId, jobId) {
            if (this.mode === "mock") return fromMock(window.MockEngine.ta.apply(userId, jobId));
            return fetchJson("/ta/applications", { method: "POST", body: JSON.stringify({ jobId }) });
        },
        async taMyApplications(userId, params) {
            if (this.mode === "mock") return fromMock(window.MockEngine.ta.listMyApplications(userId, params));
            const query = new URLSearchParams(params).toString();
            return fetchJson(`/ta/applications?${query}`, { method: "GET" });
        },

        async moDashboard() {
            if (this.mode === "mock") return fromMock(window.MockEngine.mo.dashboard());
            return fetchJson("/mo/dashboard", { method: "GET" });
        },
        async moListJobs(params, moUserId) {
            if (this.mode === "mock") return fromMock(window.MockEngine.mo.listJobs(params, moUserId));
            const query = new URLSearchParams(params).toString();
            return fetchJson(`/mo/jobs?${query}`, { method: "GET" });
        },
        async moSaveJob(payload, moUserId) {
            if (this.mode === "mock") return fromMock(window.MockEngine.mo.saveJob(payload, moUserId));
            const path = payload.jobId ? `/mo/jobs/${payload.jobId}` : "/mo/jobs";
            return fetchJson(path, { method: payload.jobId ? "PUT" : "POST", body: JSON.stringify(payload) });
        },
        async moListApplicants(params) {
            if (this.mode === "mock") return fromMock(window.MockEngine.mo.listApplicants(params));
            const query = new URLSearchParams(params).toString();
            return fetchJson(`/mo/applicants?${query}`, { method: "GET" });
        },
        async moReview(appId) {
            if (this.mode === "mock") return fromMock(window.MockEngine.mo.review(appId));
            return fetchJson(`/mo/review/${appId}`, { method: "GET" });
        },
        async moSetStatus(appId, status, reviewNote) {
            if (this.mode === "mock") return fromMock(window.MockEngine.mo.setStatus(appId, status, reviewNote));
            return fetchJson(`/mo/applications/${appId}/status`, { method: "PUT", body: JSON.stringify({ status, reviewNote }) });
        },

        async adminDashboard() {
            if (this.mode === "mock") return fromMock(window.MockEngine.admin.dashboard());
            return fetchJson("/admin/dashboard", { method: "GET" });
        },
        async adminListUsers(params) {
            if (this.mode === "mock") {
                const result = window.MockEngine.admin.listUsers(params);
                return fromMock(result, result.meta || null);
            }
            const query = new URLSearchParams(params).toString();
            return fetchJson(`/admin/users?${query}`, { method: "GET" });
        },
        async adminListApplications(params) {
            if (this.mode === "mock") return fromMock(window.MockEngine.admin.listApplications(params));
            const query = new URLSearchParams(params).toString();
            return fetchJson(`/admin/applications?${query}`, { method: "GET" });
        },
        async adminWorkload(params) {
            if (this.mode === "mock") return fromMock(window.MockEngine.admin.workload(params));
            const query = new URLSearchParams(params).toString();
            return fetchJson(`/admin/workload?${query}`, { method: "GET" });
        },

        lookups: {
            modules() {
                return window.MockEngine.lookups.modules();
            },
            jobs() {
                return window.MockEngine.lookups.jobs();
            }
        }
    };
})();
