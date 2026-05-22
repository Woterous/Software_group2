<%
    request.setAttribute("pageTitle", "MO Applicants");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="mo-applicants-page">
        <section class="panel glass-panel reveal-up">
            <div class="panel-head">
                <h2>Applicant List</h2>
                <a href="${pageContext.request.contextPath}/pages/mo/review">Go to Review Center</a>
            </div>
            <form id="mo-applicant-filter-form" class="filter-shell">
                <div class="filter-shell-grid">
                    <label class="filter-field filter-field--search">Keyword
                        <input type="text" name="keyword" placeholder="Search applicant" />
                        <button class="filter-search-btn" type="submit" aria-label="Search">
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <path d="m21 21-4.3-4.3"></path>
                                <circle cx="11" cy="11" r="7"></circle>
                            </svg>
                        </button>
                    </label>
                    <label class="filter-field">Job
                        <select name="jobId"><option value="">All Jobs</option></select>
                    </label>
                    <label class="filter-field">Status
                        <select name="status">
                            <option value="">All Status</option>
                            <option value="pending">Pending</option>
                            <option value="selected">Selected</option>
                            <option value="rejected">Rejected</option>
                        </select>
                    </label>
                </div>
            </form>

            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Applicant</th>
                        <th>Job</th>
                        <th>Skills</th>
                        <th>Status</th>
                        <th>CV</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody id="mo-applicant-table"></tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
