<%
    request.setAttribute("pageTitle", "Sign In - TA Recruitment System");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<main class="public-shell">
    <section class="auth-card glass-panel reveal-up">
        <div class="eyebrow">Welcome Back</div>
        <h2>Sign in to your workspace</h2>
        <p class="muted">Use mock credentials or register a new account to continue.</p>

        <form id="login-form" class="form-stack">
            <label>Email
                <input type="email" name="email" placeholder="name@school.edu" required />
            </label>
            <label>Password
                <input type="password" name="password" placeholder="••••••••" required />
            </label>
            <label>Role
                <select name="role" required>
                    <option value="ta">Teaching Assistant</option>
                    <option value="mo">Module Organiser</option>
                    <option value="admin">Administrator</option>
                </select>
            </label>
            <button class="primary-btn" type="submit">Sign In</button>
        </form>

        <div class="auth-links">
            <span>New here?</span>
            <a href="${pageContext.request.contextPath}/pages/register">Create account</a>
        </div>
    </section>
</main>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
