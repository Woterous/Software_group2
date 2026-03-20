<%
    request.setAttribute("pageTitle", "Register - TA Recruitment System");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<main class="public-shell">
    <section class="auth-card glass-panel reveal-up">
        <div class="eyebrow">Get Started</div>
        <h2>Create your account</h2>
        <p class="muted">This registration flow runs in mock mode for frontend integration.</p>

        <form id="register-form" class="form-grid-two">
            <label>Full Name
                <input type="text" name="name" placeholder="Alex Morgan" required />
            </label>
            <label>Email
                <input type="email" name="email" placeholder="alex@school.edu" required />
            </label>
            <label>Password
                <input type="password" name="password" placeholder="Min 8 characters" required />
            </label>
            <label>Role
                <select name="role" required>
                    <option value="ta">Teaching Assistant</option>
                    <option value="mo">Module Organiser</option>
                    <option value="admin">Administrator</option>
                </select>
            </label>
            <label class="full-row">Skills
                <input type="text" name="skills" placeholder="Java, SQL, Communication" />
            </label>
            <label class="full-row">CV Path (optional)
                <input type="text" name="cvPath" placeholder="/uploads/alex_cv.pdf" />
            </label>
            <button class="primary-btn full-row" type="submit">Create Account</button>
        </form>

        <div class="auth-links">
            <span>Already registered?</span>
            <a href="${pageContext.request.contextPath}/pages/login">Back to login</a>
        </div>
    </section>
</main>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
