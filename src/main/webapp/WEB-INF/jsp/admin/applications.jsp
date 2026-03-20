<%
    request.setAttribute("pageTitle", "Admin Applications");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="admin-applications-page">
        <section class="panel glass-panel reveal-up">
            <div class="panel-head">
                <h2>Application Monitor</h2>
                <span class="muted">Cross-role transparency and filtering</span>
            </div>
            <form id="admin-application-filter-form" class="toolbar-row">
                <select name="status">
                    <option value="">All Status</option>
                    <option value="pending">Pending</option>
                    <option value="selected">Selected</option>
                    <option value="rejected">Rejected</option>
                </select>
                <select name="module"><option value="">All Modules</option></select>
                <input type="text" name="keyword" placeholder="Search by job/applicant" />
                <button class="ghost-btn" type="submit">Filter</button>
            </form>

            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Application ID</th>
                        <th>Applicant</th>
                        <th>Job</th>
                        <th>Module</th>
                        <th>Status</th>
                        <th>Updated</th>
                    </tr>
                    </thead>
                    <tbody id="admin-application-table"></tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
