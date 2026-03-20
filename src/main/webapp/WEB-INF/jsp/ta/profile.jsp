<%
    request.setAttribute("pageTitle", "TA Profile");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="ta-profile-page">
        <section class="panel glass-panel reveal-up">
            <div class="panel-head">
                <h2>Profile Management</h2>
                <span class="muted">US-TA-003 / US-TA-004</span>
            </div>
            <form id="ta-profile-form" class="form-grid-two">
                <label>Full Name
                    <input type="text" name="name" required />
                </label>
                <label>Email
                    <input type="email" name="email" required />
                </label>
                <label class="full-row">Skills
                    <input type="text" name="skills" placeholder="Java, Data Structures, Teaching" />
                </label>
                <label class="full-row">Major
                    <input type="text" name="major" placeholder="Computer Science" />
                </label>
                <label class="full-row">Contact
                    <input type="text" name="contact" placeholder="+86 138****0000" />
                </label>
                <button class="primary-btn" type="submit">Save Profile</button>
            </form>
        </section>

        <section class="panel glass-panel reveal-up">
            <div class="panel-head">
                <h2>CV Management</h2>
                <span class="muted">Mock path-based upload</span>
            </div>
            <form id="ta-cv-form" class="form-inline">
                <input type="text" name="cvPath" placeholder="/uploads/ta_cv.pdf" required />
                <button class="primary-btn" type="submit">Update CV</button>
                <button class="ghost-btn" type="button" id="ta-cv-remove">Remove</button>
            </form>
            <p class="muted" id="ta-cv-current">Current CV: -</p>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
