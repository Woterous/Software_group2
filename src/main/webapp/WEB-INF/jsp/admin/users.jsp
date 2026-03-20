<%
    request.setAttribute("pageTitle", "Admin Users");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="admin-users-page">
        <section class="panel glass-panel reveal-up">
            <div class="panel-head">
                <h2>User Registry</h2>
                <span class="muted">Read-only governance view</span>
            </div>
            <form id="admin-user-filter-form" class="toolbar-row">
                <select name="role">
                    <option value="">All Roles</option>
                    <option value="ta">TA</option>
                    <option value="mo">MO</option>
                    <option value="admin">Admin</option>
                </select>
                <input type="text" name="keyword" placeholder="Search name or email" />
                <button class="ghost-btn" type="submit">Filter</button>
            </form>

            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>User ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Skills</th>
                    </tr>
                    </thead>
                    <tbody id="admin-user-table"></tbody>
                </table>
            </div>
            <div id="admin-users-pagination" class="pagination"></div>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
