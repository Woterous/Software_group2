<%
    request.setAttribute("pageTitle", "TA Applications");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="ta-applications-page">
        <section class="page-intro glass-panel reveal-up">
            <div>
                <span class="eyebrow eyebrow-subtle">Application pipeline</span>
                <h2>My Applications</h2>
                <p class="muted">Monitor progress, identify stalled submissions, and keep track of every decision in one place.</p>
            </div>
            <div class="page-intro-note">
                <span class="section-kicker">Review lens</span>
                <p>Use status filters to separate active submissions from final decisions and avoid missing follow-up windows.</p>
            </div>
        </section>

        <section class="panel applications-panel glass-panel reveal-up">
            <div class="panel-head panel-head--rich">
                <div>
                    <span class="section-kicker">Tracking</span>
                    <h3>Application status table</h3>
                </div>
                <span class="muted">Updated whenever the module owner records a review decision</span>
            </div>
            <form id="ta-app-filter-form" class="filter-shell filter-shell--compact">
                <div class="filter-shell-grid">
                    <label class="filter-field">Status
                        <select name="status">
                            <option value="">All Status</option>
                            <option value="pending">Pending</option>
                            <option value="selected">Selected</option>
                            <option value="rejected">Rejected</option>
                        </select>
                    </label>
                    <label class="filter-field">Keyword
                        <input type="text" name="keyword" placeholder="Search by module or role" />
                    </label>
                </div>
                <div class="filter-shell-actions">
                    <button class="ghost-btn" type="submit">Filter Results</button>
                    <p class="filter-helper">Combine status and keyword filters to review only the applications that need action.</p>
                </div>
            </form>
            <div class="table-wrap table-wrap--applications">
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
            <p class="table-footnote">If a role remains pending for too long, refresh your profile and CV before applying to additional openings.</p>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
