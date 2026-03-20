<%
    request.setAttribute("pageTitle", "Admin Dashboard");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="admin-dashboard-page">
        <section class="hero-card glass-panel reveal-up">
            <h2>Admin Command Center</h2>
            <p class="muted">Observe system health, hiring throughput, and workload fairness indicators.</p>
        </section>

        <section class="stats-grid">
            <article class="stat-card glass-panel"><h3>Total Users</h3><p id="admin-total-users">0</p></article>
            <article class="stat-card glass-panel"><h3>Open Jobs</h3><p id="admin-open-jobs">0</p></article>
            <article class="stat-card glass-panel"><h3>Total Applications</h3><p id="admin-total-apps">0</p></article>
            <article class="stat-card glass-panel"><h3>Overload Risk</h3><p id="admin-overload-count">0</p></article>
        </section>

        <section class="split-grid">
            <article class="panel glass-panel">
                <div class="panel-head"><h3>Recent Applications</h3></div>
                <div id="admin-recent-apps" class="list-stack"></div>
            </article>
            <article class="panel glass-panel">
                <div class="panel-head"><h3>Workload Alerts</h3></div>
                <div id="admin-workload-alerts" class="list-stack"></div>
            </article>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
