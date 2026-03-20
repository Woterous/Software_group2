window.PageModules = window.PageModules || {};
window.PageModules.ta = window.PageModules.ta || {};

(function () {
    function requireSession() {
        return window.UIKit.ensureSessionOrRedirect(["ta"]);
    }

    function renderList(container, rows, renderer) {
        if (!container) return;
        if (!rows.length) {
            container.innerHTML = '<div class="stack-item muted">No data available.</div>';
            return;
        }
        container.innerHTML = rows.map(renderer).join("");
    }

    async function initDashboard() {
        const session = requireSession();
        if (!session) return;
        const result = await window.ApiClient.taDashboard(session.userId);
        if (!result.success) {
            window.UIKit.toast(result.error.message, "error");
            return;
        }
        document.getElementById("ta-name").textContent = session.name;
        document.getElementById("ta-open-jobs").textContent = result.data.openJobs;
        document.getElementById("ta-submitted").textContent = result.data.submitted;
        document.getElementById("ta-pending").textContent = result.data.pending;
        document.getElementById("ta-selected").textContent = result.data.selected;

        renderList(document.getElementById("ta-latest-apps"), result.data.latestApplications, (row) => `
            <div class="stack-item">
                <strong>${window.UIKit.escapeHtml(row.title)}</strong>
                <div class="job-meta">
                    <span>${window.UIKit.escapeHtml(row.moduleName)}</span>
                    <span>${window.UIKit.badge(row.status)}</span>
                    <span>${window.UIKit.escapeHtml(row.updatedAt)}</span>
                </div>
            </div>
        `);

        renderList(document.getElementById("ta-recommended-jobs"), result.data.recommendedJobs, (row) => `
            <div class="stack-item">
                <strong>${window.UIKit.escapeHtml(row.title)}</strong>
                <div class="job-meta"><span>${window.UIKit.escapeHtml(row.moduleName)}</span><span>${window.UIKit.badge(row.status)}</span></div>
                <a href="${window.APP_CONTEXT}/pages/ta/job-detail?id=${window.UIKit.escapeHtml(row.jobId)}">View Detail</a>
            </div>
        `);
    }

    async function initProfile() {
        const session = requireSession();
        if (!session) return;

        const profileForm = document.getElementById("ta-profile-form");
        const cvForm = document.getElementById("ta-cv-form");
        const cvCurrent = document.getElementById("ta-cv-current");

        const load = async () => {
            const result = await window.ApiClient.taProfile(session.userId);
            if (!result.success) return;
            const profile = result.data.profile;
            profileForm.name.value = profile.name || "";
            profileForm.email.value = profile.email || "";
            profileForm.skills.value = (profile.skills || []).join(", ");
            profileForm.major.value = profile.major || "";
            profileForm.contact.value = profile.contact || "";
            cvForm.cvPath.value = profile.cvPath || "";
            cvCurrent.textContent = `Current CV: ${profile.cvPath || "Not uploaded"}`;
        };

        await load();

        profileForm.addEventListener("submit", async (event) => {
            event.preventDefault();
            const payload = window.UIKit.formToObject(profileForm);
            const result = await window.ApiClient.taUpdateProfile(session.userId, payload);
            if (!result.success) {
                window.UIKit.toast(result.error.message, "error");
                return;
            }
            window.UIKit.toast("Profile updated.", "success");
            await load();
        });

        cvForm.addEventListener("submit", async (event) => {
            event.preventDefault();
            const payload = window.UIKit.formToObject(cvForm);
            const result = await window.ApiClient.taUpdateCv(session.userId, payload);
            if (!result.success) {
                window.UIKit.toast(result.error.message, "error");
                return;
            }
            window.UIKit.toast("CV path updated.", "success");
            await load();
        });

        document.getElementById("ta-cv-remove").addEventListener("click", async () => {
            const result = await window.ApiClient.taUpdateCv(session.userId, { cvPath: "" });
            if (result.success) {
                window.UIKit.toast("CV removed.", "warn");
                await load();
            }
        });
    }

    async function initJobs() {
        const session = requireSession();
        if (!session) return;

        const form = document.getElementById("ta-job-filter-form");
        const listEl = document.getElementById("ta-job-list");
        const paginationEl = document.getElementById("ta-jobs-pagination");
        let currentPage = 1;

        const modules = window.ApiClient.lookups.modules();
        window.UIKit.setSelectOptions(form.module, modules, "value", "label", true, "All Modules");

        const load = async (page = 1) => {
            currentPage = page;
            const params = {
                page,
                size: 6,
                keyword: form.keyword.value.trim(),
                module: form.module.value,
                status: form.status.value
            };
            const result = await window.ApiClient.taListJobs(params);
            if (!result.success) {
                window.UIKit.toast(result.error.message, "error");
                return;
            }
            const rows = result.data.jobs;
            listEl.innerHTML = rows.map((job) => `
                <article class="job-card">
                    <div class="panel-head">
                        <h3>${window.UIKit.escapeHtml(job.title)}</h3>
                        ${window.UIKit.badge(job.status)}
                    </div>
                    <p class="muted">${window.UIKit.escapeHtml(job.description)}</p>
                    <div class="job-meta">
                        <span>Module: ${window.UIKit.escapeHtml(job.moduleName)}</span>
                        <span>Deadline: ${window.UIKit.escapeHtml(job.deadline)}</span>
                        <span>Skills: ${window.UIKit.escapeHtml(job.requiredSkills)}</span>
                    </div>
                    <div class="button-row">
                        <a class="ghost-btn inline" href="${window.APP_CONTEXT}/pages/ta/job-detail?id=${window.UIKit.escapeHtml(job.jobId)}">View Details</a>
                        <button class="primary-btn" type="button" data-apply="${window.UIKit.escapeHtml(job.jobId)}">Quick Apply</button>
                    </div>
                </article>
            `).join("");

            listEl.querySelectorAll("[data-apply]").forEach((btn) => {
                btn.addEventListener("click", () => {
                    const jobId = btn.dataset.apply;
                    window.UIKit.openModal({
                        title: "Submit Application",
                        message: "Submit this application now?",
                        onConfirm: async () => {
                            const applyResult = await window.ApiClient.taApply(session.userId, jobId);
                            if (!applyResult.success) {
                                window.UIKit.toast(applyResult.error.message, "error");
                                return;
                            }
                            window.UIKit.toast("Application submitted.", "success");
                            await load(currentPage);
                        }
                    });
                });
            });

            window.UIKit.renderPagination(paginationEl, result.meta, load);
        };

        form.addEventListener("submit", (event) => {
            event.preventDefault();
            load(1);
        });

        await load(1);
    }

    async function initJobDetail() {
        const session = requireSession();
        if (!session) return;

        let jobId = window.UIKit.getQuery("id");
        if (!jobId) {
            const first = window.ApiClient.lookups.jobs()[0];
            jobId = first?.jobId;
        }
        if (!jobId) return;

        const result = await window.ApiClient.taJobDetail(jobId);
        if (!result.success) {
            window.UIKit.toast(result.error.message, "error");
            return;
        }

        const job = result.data.job;
        const card = document.getElementById("ta-job-detail-card");
        card.innerHTML = `
            <div class="panel-head"><h2>${window.UIKit.escapeHtml(job.title)}</h2>${window.UIKit.badge(job.status)}</div>
            <p class="muted">${window.UIKit.escapeHtml(job.description)}</p>
            <div class="job-meta">
                <span>Job ID: ${window.UIKit.escapeHtml(job.jobId)}</span>
                <span>Module: ${window.UIKit.escapeHtml(job.moduleName)}</span>
                <span>Deadline: ${window.UIKit.escapeHtml(job.deadline)}</span>
                <span>Required Skills: ${window.UIKit.escapeHtml(job.requiredSkills)}</span>
            </div>
        `;

        const applyBtn = document.getElementById("ta-apply-btn");
        applyBtn.addEventListener("click", () => {
            window.UIKit.openModal({
                title: "Confirm Apply",
                message: `Apply for ${job.title}?`,
                onConfirm: async () => {
                    const applyResult = await window.ApiClient.taApply(session.userId, job.jobId);
                    if (!applyResult.success) {
                        window.UIKit.toast(applyResult.error.message, "error");
                        return;
                    }
                    window.UIKit.toast("Application submitted.", "success");
                }
            });
        });
    }

    async function initApplications() {
        const session = requireSession();
        if (!session) return;

        const form = document.getElementById("ta-app-filter-form");
        const tbody = document.getElementById("ta-app-table");

        const load = async () => {
            const params = {
                status: form.status.value,
                keyword: form.keyword.value.trim()
            };
            const result = await window.ApiClient.taMyApplications(session.userId, params);
            if (!result.success) {
                window.UIKit.toast(result.error.message, "error");
                return;
            }
            const rows = result.data.applications;
            tbody.innerHTML = rows.map((row) => `
                <tr>
                    <td>${window.UIKit.escapeHtml(row.applicationId)}</td>
                    <td>${window.UIKit.escapeHtml(row.title)}</td>
                    <td>${window.UIKit.escapeHtml(row.moduleName)}</td>
                    <td>${window.UIKit.badge(row.status)}</td>
                    <td>${window.UIKit.escapeHtml(row.updatedAt)}</td>
                </tr>
            `).join("");
        };

        form.addEventListener("submit", (event) => {
            event.preventDefault();
            load();
        });

        await load();
    }

    window.PageModules.ta.dashboard = initDashboard;
    window.PageModules.ta.profile = initProfile;
    window.PageModules.ta.jobs = initJobs;
    window.PageModules.ta["job-detail"] = initJobDetail;
    window.PageModules.ta.applications = initApplications;
})();
