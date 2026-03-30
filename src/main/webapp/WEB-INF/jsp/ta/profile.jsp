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
                <span class="muted">File upload (.pdf, .doc, .docx, max 5MB)</span>
            </div>
            <form id="ta-cv-form" class="cv-upload-form">
                <input id="ta-cv-file-input" class="cv-upload-input" type="file" name="cvFile" accept=".pdf,.doc,.docx" />
                <div id="ta-cv-dropzone" class="cv-dropzone" tabindex="0" role="button" aria-label="Drop CV file here or choose file">
                    <div class="cv-dropzone-inner">
                        <div class="cv-dropzone-title">Drop your CV here</div>
                        <div class="cv-dropzone-hint">PDF, DOC, or DOCX up to 5MB</div>
                        <button id="ta-cv-pick-btn" class="glass-secondary-btn inline cv-pick-btn" type="button">Choose File</button>
                        <div id="ta-cv-selected-file" class="cv-dropzone-file">No file selected</div>
                    </div>
                </div>
                <div class="cv-upload-actions">
                    <button class="primary-btn" type="submit">Upload CV</button>
                    <button class="ghost-btn" type="button" id="ta-cv-remove">Remove</button>
                </div>
            </form>
            <p class="muted" id="ta-cv-current">Current CV: -</p>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
