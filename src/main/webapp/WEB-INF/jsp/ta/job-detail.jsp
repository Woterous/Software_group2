<%
    request.setAttribute("pageTitle", "TA Job Detail");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="ta-job-detail-page">
        <div class="job-detail-topbar">
            <a class="glass-secondary-btn inline" href="${pageContext.request.contextPath}/pages/ta/jobs">Back to Job Board</a>
        </div>
        <section class="page-intro glass-panel reveal-up">
            <div>
                <span class="eyebrow eyebrow-subtle">Role details</span>
                <h2>Review role scope before you apply</h2>
                <p class="muted">Use the detail view to assess deadline pressure, required skills, and module fit before sending your application.</p>
            </div>
        </section>
        <article class="panel glass-panel reveal-up" id="ta-job-detail-card"></article>

        <section class="panel glass-panel job-apply-panel">
            <div class="job-apply-copy">
                <h3>Application Confirmation</h3>
                <p class="muted">Submit only after checking deadline urgency, module alignment, and document readiness.</p>
            </div>
            <div class="job-detail-actions">
                <button id="ta-apply-btn" class="primary-btn" type="button">Apply Now</button>
            </div>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
