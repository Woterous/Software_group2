<%
    request.setAttribute("pageTitle", "Admin Workload");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="admin-workload-page">
        <section class="panel glass-panel reveal-up">
            <div class="panel-head">
                <h2>TA Workload Overview</h2>
                <span class="muted">Sort and risk-highlight by selected assignments</span>
            </div>

            <form id="admin-workload-filter-form" class="toolbar-row">
                <select name="riskLevel">
                    <option value="">All Levels</option>
                    <option value="normal">Normal</option>
                    <option value="warning">Warning</option>
                    <option value="overload">Overload</option>
                </select>
                <button class="ghost-btn" type="submit">Filter</button>
            </form>

            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>TA</th>
                        <th>Selected Modules</th>
                        <th>Total Hours/Week</th>
                        <th>Risk Level</th>
                    </tr>
                    </thead>
                    <tbody id="admin-workload-table"></tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
