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
            <form id="admin-user-filter-form" class="filter-shell">
                <div class="filter-shell-grid">
                    <label class="filter-field filter-field--search">Keyword
                        <input type="text" name="keyword" placeholder="Search name or email" />
                        <button class="filter-search-btn" type="submit" aria-label="Search">
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <path d="m21 21-4.3-4.3"></path>
                                <circle cx="11" cy="11" r="7"></circle>
                            </svg>
                        </button>
                    </label>
                    <label class="filter-field">Role
                        <select name="role">
                            <option value="">All Roles</option>
                            <option value="ta">TA</option>
                            <option value="mo">MO</option>
                            <option value="admin">Admin</option>
                        </select>
                    </label>
                </div>
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
