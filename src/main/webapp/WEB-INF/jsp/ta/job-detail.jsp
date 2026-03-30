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
        <article class="panel glass-panel reveal-up" id="ta-job-detail-card"></article>

        <section class="panel glass-panel job-apply-panel">
            <div class="job-apply-copy">
                <h3>Application Confirmation</h3>
                <p class="muted">Submitting in mock mode will create a deterministic pending record.</p>
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
