<%
    request.setAttribute("pageTitle", "TA Job Board");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="ta-jobs-page">
        <section class="panel glass-panel reveal-up">
            <div class="panel-head">
                <h2>Available TA Positions</h2>
                <span class="muted">Filter, sort, and apply in mock mode</span>
            </div>
            <form id="ta-job-filter-form" class="toolbar-row">
                <input type="text" name="keyword" placeholder="Search by title/module" />
                <select name="module">
                    <option value="">All Modules</option>
                </select>
                <select name="status">
                    <option value="">Any Status</option>
                    <option value="open">Open</option>
                    <option value="closing">Closing Soon</option>
                </select>
                <button class="ghost-btn" type="submit">Apply Filter</button>
            </form>
            <div id="ta-job-list" class="card-list"></div>
            <div id="ta-jobs-pagination" class="pagination"></div>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
