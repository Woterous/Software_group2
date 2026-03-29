(function () {
    const DB_KEY = "tars.mock.db.v1";
    const SESSION_KEY = "tars.mock.session.v1";

    const clone = (obj) => JSON.parse(JSON.stringify(obj));

    function nowDate() {
        return new Date().toISOString().slice(0, 10);
    }

    function readDb() {
        const raw = localStorage.getItem(DB_KEY);
        if (!raw) {
            const seed = clone(window.TARS_MOCK_SEED);
            localStorage.setItem(DB_KEY, JSON.stringify(seed));
            return seed;
        }
        return JSON.parse(raw);
    }

    function writeDb(db) {
        localStorage.setItem(DB_KEY, JSON.stringify(db));
        return db;
    }

    function getSession() {
        const raw = localStorage.getItem(SESSION_KEY);
        return raw ? JSON.parse(raw) : null;
    }

    function setSession(user) {
        if (!user) {
            localStorage.removeItem(SESSION_KEY);
            return;
        }
        localStorage.setItem(SESSION_KEY, JSON.stringify({
            userId: user.userId,
            role: user.role,
            name: user.name,
            email: user.email
        }));
    }

    function buildWorkload(db) {
        const selectedApps = db.applications.filter((a) => a.status === "selected");
        const taUsers = db.users.filter((u) => u.role === "ta");

        return taUsers.map((user) => {
            const apps = selectedApps.filter((a) => a.userId === user.userId);
            const hours = apps.reduce((sum, app) => {
                const job = db.jobs.find((j) => j.jobId === app.jobId);
                return sum + (job ? job.weeklyHours : 0);
            }, 0);
            let riskLevel = "normal";
            if (hours >= 28) riskLevel = "overload";
            else if (hours >= 20) riskLevel = "warning";
            return {
                userId: user.userId,
                name: user.name,
                selectedModules: apps.length,
                totalHours: hours,
                riskLevel
            };
        });
    }

    function nextId(prefix, list, field) {
        const max = list.reduce((m, item) => {
            const n = Number(String(item[field]).replace(prefix, ""));
            return Number.isFinite(n) ? Math.max(m, n) : m;
        }, 0);
        return `${prefix}${String(max + 1).padStart(3, "0")}`;
    }

    function paginate(items, page = 1, size = 8) {
        const p = Math.max(1, Number(page) || 1);
        const s = Math.max(1, Number(size) || 8);
        const totalItems = items.length;
        const totalPages = Math.max(1, Math.ceil(totalItems / s));
        const start = (p - 1) * s;
        return {
            items: items.slice(start, start + s),
            meta: { page: p, size: s, totalItems, totalPages }
        };
    }

    function matchKeyword(target, keyword) {
        if (!keyword) return true;
        return String(target || "").toLowerCase().includes(String(keyword).toLowerCase());
    }

    window.MockEngine = {
        reset() {
            localStorage.setItem(DB_KEY, JSON.stringify(clone(window.TARS_MOCK_SEED)));
            localStorage.removeItem(SESSION_KEY);
        },

        getSession,
        setSession,

        auth: {
            login({ email, password, role }) {
                const db = readDb();
                const user = db.users.find((u) => u.email === email && u.password === password && u.role === role);
                if (!user) {
                    return { ok: false, error: { code: "AUTH_INVALID_CREDENTIALS", message: "Invalid credentials or role." } };
                }
                setSession(user);
                return { ok: true, data: { user: clone(user) } };
            },
            register(payload) {
                const db = readDb();
                const exists = db.users.some((u) => u.email === payload.email);
                if (exists) {
                    return { ok: false, error: { code: "AUTH_EMAIL_EXISTS", message: "Email already exists." } };
                }
                const role = payload.role || "ta";
                const prefix = role === "mo" ? "MO" : role === "admin" ? "AD" : "TA";
                const user = {
                    userId: nextId(prefix, db.users, "userId"),
                    name: payload.name,
                    email: payload.email,
                    password: payload.password,
                    role,
                    skills: payload.skills ? payload.skills.split(",").map((x) => x.trim()).filter(Boolean) : [],
                    major: payload.major || "",
                    contact: payload.contact || "",
                    cvPath: payload.cvPath || ""
                };
                db.users.push(user);
                writeDb(db);
                return { ok: true, data: { user: clone(user) } };
            },
            logout() {
                setSession(null);
                return { ok: true, data: { loggedOut: true } };
            },
            me() {
                const session = getSession();
                if (!session) return { ok: false, error: { code: "AUTH_NOT_LOGIN", message: "Not logged in." } };
                const db = readDb();
                const user = db.users.find((u) => u.userId === session.userId);
                if (!user) return { ok: false, error: { code: "AUTH_NOT_FOUND", message: "Session user missing." } };
                return { ok: true, data: { user: clone(user) } };
            }
        },

        ta: {
            dashboard(userId) {
                const db = readDb();
                const apps = db.applications.filter((a) => a.userId === userId);
                const latest = apps.slice().sort((a, b) => (a.updatedAt < b.updatedAt ? 1 : -1)).slice(0, 4).map((app) => {
                    const job = db.jobs.find((j) => j.jobId === app.jobId);
                    return { ...app, title: job?.title || "Unknown", moduleName: job?.moduleName || "-" };
                });
                const recJobs = db.jobs.filter((j) => j.status !== "closed").slice(0, 4);
                return {
                    ok: true,
                    data: {
                        openJobs: db.jobs.filter((j) => j.status !== "closed").length,
                        submitted: apps.length,
                        pending: apps.filter((a) => a.status === "pending").length,
                        selected: apps.filter((a) => a.status === "selected").length,
                        latestApplications: latest,
                        recommendedJobs: recJobs
                    }
                };
            },
            getProfile(userId) {
                const db = readDb();
                const user = db.users.find((u) => u.userId === userId);
                if (!user) return { ok: false, error: { code: "USER_NOT_FOUND", message: "User not found" } };
                return { ok: true, data: { profile: clone(user) } };
            },
            updateProfile(userId, payload) {
                const db = readDb();
                const user = db.users.find((u) => u.userId === userId);
                if (!user) return { ok: false, error: { code: "USER_NOT_FOUND", message: "User not found" } };
                user.name = payload.name ?? user.name;
                user.email = payload.email ?? user.email;
                user.skills = payload.skills ? payload.skills.split(",").map((x) => x.trim()).filter(Boolean) : user.skills;
                user.major = payload.major ?? user.major;
                user.contact = payload.contact ?? user.contact;
                writeDb(db);
                return { ok: true, data: { profile: clone(user) } };
            },
            updateCv(userId, cvPath) {
                const db = readDb();
                const user = db.users.find((u) => u.userId === userId);
                if (!user) return { ok: false, error: { code: "USER_NOT_FOUND", message: "User not found" } };
                user.cvPath = cvPath || "";
                writeDb(db);
                return { ok: true, data: { cvPath: user.cvPath } };
            },
            listJobs(params) {
                const db = readDb();
                let jobs = db.jobs.slice();
                if (params.module) jobs = jobs.filter((j) => j.moduleName === params.module);
                if (params.status) jobs = jobs.filter((j) => j.status === params.status);
                if (params.keyword) jobs = jobs.filter((j) => matchKeyword(`${j.title} ${j.moduleName} ${j.description}`, params.keyword));
                const paged = paginate(jobs, params.page, params.size);
                return { ok: true, data: { jobs: paged.items }, meta: paged.meta };
            },
            getJobDetail(jobId) {
                const db = readDb();
                const job = db.jobs.find((j) => j.jobId === jobId);
                if (!job) return { ok: false, error: { code: "JOB_NOT_FOUND", message: "Job not found" } };
                return { ok: true, data: { job: clone(job) } };
            },
            apply(userId, jobId) {
                const db = readDb();
                const existing = db.applications.find((a) => a.userId === userId && a.jobId === jobId);
                if (existing) {
                    return { ok: false, error: { code: "APPLICATION_DUPLICATE", message: "You already applied for this job." } };
                }
                const job = db.jobs.find((j) => j.jobId === jobId);
                if (!job) {
                    return { ok: false, error: { code: "JOB_NOT_FOUND", message: "Cannot apply: job missing." } };
                }
                const app = {
                    applicationId: nextId("APP", db.applications, "applicationId"),
                    userId,
                    jobId,
                    status: "pending",
                    reviewNote: "",
                    updatedAt: nowDate()
                };
                db.applications.push(app);
                writeDb(db);
                return { ok: true, data: { application: clone(app) } };
            },
            listMyApplications(userId, params) {
                const db = readDb();
                let apps = db.applications
                    .filter((a) => a.userId === userId)
                    .map((app) => {
                        const job = db.jobs.find((j) => j.jobId === app.jobId);
                        return {
                            ...app,
                            title: job?.title || "Unknown",
                            moduleName: job?.moduleName || "-"
                        };
                    });
                if (params.status) apps = apps.filter((a) => a.status === params.status);
                if (params.keyword) apps = apps.filter((a) => matchKeyword(`${a.title} ${a.moduleName}`, params.keyword));
                return { ok: true, data: { applications: apps } };
            }
        },

        mo: {
            dashboard() {
                const db = readDb();
                const activeJobs = db.jobs.filter((j) => j.status !== "closed");
                const totalApplicants = db.applications.length;
                return {
                    ok: true,
                    data: {
                        activeJobs: activeJobs.length,
                        totalApplicants,
                        pendingReview: db.applications.filter((a) => a.status === "pending").length,
                        selectedCount: db.applications.filter((a) => a.status === "selected").length,
                        nearDeadline: activeJobs
                            .slice()
                            .sort((a, b) => (a.deadline > b.deadline ? 1 : -1))
                            .slice(0, 5)
                    }
                };
            },
            listJobs(params, moUserId) {
                const db = readDb();
                let jobs = db.jobs.filter((j) => !moUserId || j.postedBy === moUserId);
                if (params.status) jobs = jobs.filter((j) => j.status === params.status);
                if (params.keyword) jobs = jobs.filter((j) => matchKeyword(`${j.title} ${j.moduleName}`, params.keyword));

                const withApplicants = jobs.map((j) => ({
                    ...j,
                    applicantCount: db.applications.filter((a) => a.jobId === j.jobId).length
                }));
                return { ok: true, data: { jobs: withApplicants } };
            },
            saveJob(payload, moUserId) {
                const db = readDb();
                if (payload.jobId) {
                    const existing = db.jobs.find((j) => j.jobId === payload.jobId);
                    if (!existing) return { ok: false, error: { code: "JOB_NOT_FOUND", message: "Job not found" } };
                    Object.assign(existing, payload);
                    writeDb(db);
                    return { ok: true, data: { job: clone(existing) } };
                }
                const job = {
                    jobId: nextId("JOB", db.jobs, "jobId"),
                    title: payload.title,
                    moduleName: payload.moduleName,
                    requiredSkills: payload.requiredSkills,
                    deadline: payload.deadline,
                    description: payload.description,
                    status: payload.status || "open",
                    postedBy: moUserId,
                    weeklyHours: Number(payload.weeklyHours || 6),
                    createdAt: nowDate()
                };
                db.jobs.push(job);
                writeDb(db);
                return { ok: true, data: { job: clone(job) } };
            },
            listApplicants(params) {
                const db = readDb();
                let rows = db.applications.map((app) => {
                    const user = db.users.find((u) => u.userId === app.userId);
                    const job = db.jobs.find((j) => j.jobId === app.jobId);
                    return {
                        ...app,
                        applicantName: user?.name || "Unknown",
                        applicantSkills: (user?.skills || []).join(", "),
                        cvPath: user?.cvPath || "",
                        title: job?.title || "Unknown",
                        moduleName: job?.moduleName || "-"
                    };
                });
                if (params.jobId) rows = rows.filter((r) => r.jobId === params.jobId);
                if (params.status) rows = rows.filter((r) => r.status === params.status);
                if (params.keyword) rows = rows.filter((r) => matchKeyword(`${r.applicantName} ${r.title} ${r.moduleName}`, params.keyword));
                return { ok: true, data: { applicants: rows } };
            },
            review(appId) {
                const db = readDb();
                const app = db.applications.find((a) => a.applicationId === appId) || db.applications[0];
                const user = db.users.find((u) => u.userId === app.userId);
                const job = db.jobs.find((j) => j.jobId === app.jobId);
                return {
                    ok: true,
                    data: {
                        application: {
                            ...app,
                            applicantName: user?.name || "Unknown",
                            applicantSkills: user?.skills || [],
                            cvPath: user?.cvPath || "",
                            title: job?.title || "Unknown",
                            moduleName: job?.moduleName || "-",
                            requiredSkills: job?.requiredSkills || ""
                        }
                    }
                };
            },
            setStatus(appId, status, reviewNote) {
                const db = readDb();
                const app = db.applications.find((a) => a.applicationId === appId);
                if (!app) return { ok: false, error: { code: "APPLICATION_NOT_FOUND", message: "Application not found" } };
                app.status = status;
                app.reviewNote = reviewNote || "";
                app.updatedAt = nowDate();
                writeDb(db);
                return { ok: true, data: { application: clone(app) } };
            }
        },

        admin: {
            dashboard() {
                const db = readDb();
                const workload = buildWorkload(db);
                const overload = workload.filter((x) => x.riskLevel === "overload");
                const recent = db.applications
                    .slice()
                    .sort((a, b) => (a.updatedAt < b.updatedAt ? 1 : -1))
                    .slice(0, 5)
                    .map((app) => {
                        const user = db.users.find((u) => u.userId === app.userId);
                        const job = db.jobs.find((j) => j.jobId === app.jobId);
                        return {
                            ...app,
                            applicantName: user?.name || "Unknown",
                            title: job?.title || "Unknown"
                        };
                    });
                return {
                    ok: true,
                    data: {
                        totalUsers: db.users.length,
                        openJobs: db.jobs.filter((j) => j.status !== "closed").length,
                        totalApplications: db.applications.length,
                        overloadCount: overload.length,
                        recentApplications: recent,
                        overloadUsers: overload
                    }
                };
            },
            listUsers(params) {
                const db = readDb();
                let users = db.users.slice();
                if (params.role) users = users.filter((u) => u.role === params.role);
                if (params.keyword) users = users.filter((u) => matchKeyword(`${u.name} ${u.email} ${u.userId}`, params.keyword));
                const paged = paginate(users, params.page, params.size);
                return { ok: true, data: { users: paged.items }, meta: paged.meta };
            },
            listApplications(params) {
                const db = readDb();
                let rows = db.applications.map((app) => {
                    const user = db.users.find((u) => u.userId === app.userId);
                    const job = db.jobs.find((j) => j.jobId === app.jobId);
                    return {
                        ...app,
                        applicantName: user?.name || "Unknown",
                        title: job?.title || "Unknown",
                        moduleName: job?.moduleName || "-"
                    };
                });
                if (params.status) rows = rows.filter((r) => r.status === params.status);
                if (params.module) rows = rows.filter((r) => r.moduleName === params.module);
                if (params.keyword) rows = rows.filter((r) => matchKeyword(`${r.applicantName} ${r.title}`, params.keyword));
                return { ok: true, data: { applications: rows } };
            },
            workload(params) {
                const db = readDb();
                let rows = buildWorkload(db);
                if (params.riskLevel) rows = rows.filter((r) => r.riskLevel === params.riskLevel);
                return { ok: true, data: { workload: rows } };
            }
        },

        lookups: {
            modules() {
                const db = readDb();
                return [...new Set(db.jobs.map((j) => j.moduleName))].sort();
            },
            jobs() {
                const db = readDb();
                return db.jobs.slice();
            }
        }
    };
})();
