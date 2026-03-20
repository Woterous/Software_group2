<%
    request.setAttribute("pageTitle", "MO Job Management");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="mo-jobs-page">
        <section class="panel glass-panel reveal-up">
            <div class="panel-head">
                <h2>My Job Listings</h2>
                <button type="button" class="primary-btn" id="mo-open-create-job">Post New Job</button>
            </div>
            <form id="mo-job-filter-form" class="toolbar-row">
                <input type="text" name="keyword" placeholder="Search jobs" />
                <select name="status">
                    <option value="">All Status</option>
                    <option value="open">Open</option>
                    <option value="closing">Closing Soon</option>
                    <option value="closed">Closed</option>
                </select>
                <button class="ghost-btn" type="submit">Filter</button>
            </form>
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Job ID</th>
                        <th>Title</th>
                        <th>Module</th>
                        <th>Deadline</th>
                        <th>Status</th>
                        <th>Applicants</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody id="mo-job-table"></tbody>
                </table>
            </div>
        </section>

        <section class="panel glass-panel hidden" id="mo-job-editor">
            <div class="panel-head"><h3>Create / Edit Job</h3></div>
            <form id="mo-job-form" class="form-grid-two">
                <label>Title<input type="text" name="title" required /></label>
                <label>Module<input type="text" name="moduleName" required /></label>
                <label>Deadline<input type="date" name="deadline" required /></label>
                <label>Status
                    <select name="status"><option value="open">Open</option><option value="closing">Closing Soon</option><option value="closed">Closed</option></select>
                </label>
                <label class="full-row">Required Skills<input type="text" name="requiredSkills" required /></label>
                <label class="full-row">Description<textarea name="description" rows="4" required></textarea></label>
                <button class="primary-btn" type="submit">Save Job</button>
                <button class="ghost-btn" type="button" id="mo-cancel-job">Cancel</button>
            </form>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
