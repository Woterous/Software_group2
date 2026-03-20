window.TARS_MOCK_SEED = {
    users: [
        { userId: "TA001", name: "Emma Thompson", email: "emma@school.edu", password: "Password123!", role: "ta", skills: ["Java", "OOP", "Tutoring"], major: "Software Engineering", contact: "+86-13800010001", cvPath: "/uploads/emma_cv.pdf" },
        { userId: "TA002", name: "James Wilson", email: "james@school.edu", password: "Password123!", role: "ta", skills: ["Algorithms", "Python", "Data Structures"], major: "Computer Science", contact: "+86-13800010002", cvPath: "/uploads/james_cv.pdf" },
        { userId: "TA003", name: "Sophie Lee", email: "sophie@school.edu", password: "Password123!", role: "ta", skills: ["SQL", "DB Design", "Lab Support"], major: "Information Systems", contact: "+86-13800010003", cvPath: "/uploads/sophie_cv.pdf" },
        { userId: "TA004", name: "Michael Brown", email: "michael@school.edu", password: "Password123!", role: "ta", skills: ["Networks", "Linux", "Debugging"], major: "Network Engineering", contact: "+86-13800010004", cvPath: "" },

        { userId: "MO001", name: "Dr. Kevin Zhao", email: "kevin.zhao@school.edu", password: "Password123!", role: "mo", skills: ["Software Engineering"], major: "Faculty", contact: "+86-13800020001", cvPath: "" },
        { userId: "MO002", name: "Dr. Olivia Davis", email: "olivia.davis@school.edu", password: "Password123!", role: "mo", skills: ["Data Systems"], major: "Faculty", contact: "+86-13800020002", cvPath: "" },

        { userId: "AD001", name: "Admin Chen", email: "admin.chen@school.edu", password: "Password123!", role: "admin", skills: ["Governance"], major: "Operations", contact: "+86-13800030001", cvPath: "" }
    ],
    jobs: [
        { jobId: "JOB001", title: "TA for Software Engineering", moduleName: "EBU6304", requiredSkills: "Java, OOP, Teamwork", deadline: "2026-04-06", description: "Support labs, assist marking, and host consultation sessions.", status: "open", postedBy: "MO001", weeklyHours: 8, createdAt: "2026-03-20" },
        { jobId: "JOB002", title: "TA for Data Structures", moduleName: "EBU6301", requiredSkills: "Algorithms, Data Structures", deadline: "2026-04-03", description: "Assist with tutorials and assignment Q&A.", status: "closing", postedBy: "MO001", weeklyHours: 6, createdAt: "2026-03-19" },
        { jobId: "JOB003", title: "TA for Database Systems", moduleName: "EBU6305", requiredSkills: "SQL, Database Design", deadline: "2026-04-12", description: "Guide students in database labs and review schema design.", status: "open", postedBy: "MO002", weeklyHours: 7, createdAt: "2026-03-21" },
        { jobId: "JOB004", title: "TA for Computer Networks", moduleName: "EBU6302", requiredSkills: "Networking Protocols, Wireshark", deadline: "2026-04-18", description: "Support lab experiments and networking troubleshooting.", status: "open", postedBy: "MO002", weeklyHours: 5, createdAt: "2026-03-22" },
        { jobId: "JOB005", title: "TA for Programming Fundamentals", moduleName: "EBU5201", requiredSkills: "Python Basics, Communication", deadline: "2026-04-01", description: "Help beginners with coding exercises and office hours.", status: "closing", postedBy: "MO001", weeklyHours: 9, createdAt: "2026-03-18" }
    ],
    applications: [
        { applicationId: "APP001", userId: "TA001", jobId: "JOB001", status: "pending", reviewNote: "", updatedAt: "2026-03-23" },
        { applicationId: "APP002", userId: "TA001", jobId: "JOB002", status: "selected", reviewNote: "Strong delivery skills", updatedAt: "2026-03-22" },
        { applicationId: "APP003", userId: "TA002", jobId: "JOB003", status: "pending", reviewNote: "", updatedAt: "2026-03-23" },
        { applicationId: "APP004", userId: "TA003", jobId: "JOB003", status: "rejected", reviewNote: "Schedule conflict", updatedAt: "2026-03-21" },
        { applicationId: "APP005", userId: "TA004", jobId: "JOB004", status: "pending", reviewNote: "", updatedAt: "2026-03-24" }
    ],
    notifications: [
        { id: "N001", role: "ta", text: "Two positions are closing this week.", level: "info" },
        { id: "N002", role: "mo", text: "3 new applications arrived overnight.", level: "info" },
        { id: "N003", role: "admin", text: "One TA is approaching overload threshold.", level: "warn" }
    ]
};
