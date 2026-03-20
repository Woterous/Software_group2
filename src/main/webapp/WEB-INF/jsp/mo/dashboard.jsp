<%
    request.setAttribute("pageTitle", "MO Dashboard");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="mo-dashboard-page">
        <section class="hero-card glass-panel reveal-up">
            <h2>Module Organiser Dashboard</h2>
            <p class="muted">Manage openings, review candidates, and finalize assignments.</p>
        </section>
        <section class="stats-grid">
            <article class="stat-card glass-panel"><h3>Active Jobs</h3><p id="mo-active-jobs">0</p></article>
            <article class="stat-card glass-panel"><h3>Total Applicants</h3><p id="mo-total-applicants">0</p></article>
            <article class="stat-card glass-panel"><h3>Pending Review</h3><p id="mo-pending-review">0</p></article>
            <article class="stat-card glass-panel"><h3>Selected</h3><p id="mo-selected-count">0</p></article>
        </section>
        <section class="panel glass-panel">
            <div class="panel-head">
                <h3>Jobs Near Deadline</h3>
                <a href="${pageContext.request.contextPath}/pages/mo/jobs">Manage Jobs</a>
            </div>
            <div id="mo-near-deadline" class="list-stack"></div>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
