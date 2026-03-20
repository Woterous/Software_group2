<%
    request.setAttribute("pageTitle", "TA Dashboard");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="ta-dashboard-page">
        <section class="hero-card glass-panel reveal-up">
            <h2>Welcome back, <span id="ta-name">TA User</span></h2>
            <p class="muted">Track your applications, update profile, and apply for new opportunities.</p>
        </section>

        <section class="stats-grid">
            <article class="stat-card glass-panel"><h3>Open Jobs</h3><p id="ta-open-jobs">0</p></article>
            <article class="stat-card glass-panel"><h3>Submitted</h3><p id="ta-submitted">0</p></article>
            <article class="stat-card glass-panel"><h3>Pending</h3><p id="ta-pending">0</p></article>
            <article class="stat-card glass-panel"><h3>Selected</h3><p id="ta-selected">0</p></article>
        </section>

        <section class="split-grid">
            <article class="panel glass-panel">
                <div class="panel-head">
                    <h3>Latest Applications</h3>
                    <a href="${pageContext.request.contextPath}/pages/ta/applications">View all</a>
                </div>
                <div id="ta-latest-apps" class="list-stack"></div>
            </article>
            <article class="panel glass-panel">
                <div class="panel-head">
                    <h3>Recommended Jobs</h3>
                    <a href="${pageContext.request.contextPath}/pages/ta/jobs">Explore jobs</a>
                </div>
                <div id="ta-recommended-jobs" class="list-stack"></div>
            </article>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
