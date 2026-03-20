<%
    request.setAttribute("pageTitle", "TA Applications");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="ta-applications-page">
        <section class="panel glass-panel reveal-up">
            <div class="panel-head">
                <h2>My Applications</h2>
                <span class="muted">Status tracking preview</span>
            </div>
            <form id="ta-app-filter-form" class="toolbar-row">
                <select name="status">
                    <option value="">All Status</option>
                    <option value="pending">Pending</option>
                    <option value="selected">Selected</option>
                    <option value="rejected">Rejected</option>
                </select>
                <input type="text" name="keyword" placeholder="Search by module" />
                <button class="ghost-btn" type="submit">Filter</button>
            </form>
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Application ID</th>
                        <th>Job</th>
                        <th>Module</th>
                        <th>Status</th>
                        <th>Updated</th>
                    </tr>
                    </thead>
                    <tbody id="ta-app-table"></tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
