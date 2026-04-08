<%
    request.setAttribute("pageTitle", "TA Profile");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="ta-profile-page">
        <section class="page-intro glass-panel reveal-up">
            <div>
                <span class="eyebrow eyebrow-subtle">Profile settings</span>
                <h2>Profile and CV readiness</h2>
                <p class="muted">Keep your details accurate so module owners can quickly assess your fit, availability, and academic background.</p>
            </div>
            <div class="page-intro-note">
                <span class="section-kicker">Why it matters</span>
                <p>Clear skills, up-to-date contact details, and a polished CV make shortlisting noticeably faster.</p>
            </div>
        </section>

        <section class="settings-layout">
            <section class="panel settings-panel glass-panel reveal-up">
                <div class="panel-head panel-head--rich">
                    <div>
                        <span class="section-kicker">Account details</span>
                        <h3>Profile Management</h3>
                    </div>
                    <span class="muted">US-TA-003 / US-TA-004</span>
                </div>
                <p class="panel-description">Use concise, role-relevant language so your profile reads like a serious academic application rather than a rough draft.</p>
                <form id="ta-profile-form" class="form-grid-two form-grid-two--profile">
                    <label>Full Name
                        <input type="text" name="name" required />
                    </label>
                    <label>Email
                        <input type="email" name="email" required />
                    </label>
                    <label class="full-row">Skills
                        <input type="text" name="skills" placeholder="Java, Data Structures, Teaching" />
                    </label>
                    <label>Major
                        <input type="text" name="major" placeholder="Computer Science" />
                    </label>
                    <label>Contact
                        <input type="text" name="contact" placeholder="+86 138****0000" />
                    </label>
                    <div class="form-actions full-row">
                        <button class="primary-btn" type="submit">Save Profile</button>
                        <span class="helper-inline">Keep skills specific, current, and relevant to teaching support work.</span>
                    </div>
                </form>
            </section>

            <aside class="panel profile-side-card glass-panel reveal-up">
                <span class="section-kicker">Readiness</span>
                <h3>Profile checklist</h3>
                <div class="checklist-stack">
                    <div class="checklist-item">
                        <strong>Clear academic identity</strong>
                        <span>Name, major, and contact details should read as complete and trustworthy.</span>
                    </div>
                    <div class="checklist-item">
                        <strong>Specific skills</strong>
                        <span>Use skill phrases a module owner can assess quickly during review.</span>
                    </div>
                    <div class="checklist-item">
                        <strong>Recent CV upload</strong>
                        <span>Upload a current version whenever coursework, grades, or experience changes.</span>
                    </div>
                </div>
            </aside>
        </section>

        <section class="panel upload-panel glass-panel reveal-up">
            <div class="panel-head panel-head--rich">
                <div>
                    <span class="section-kicker">Documents</span>
                    <h3>CV Management</h3>
                </div>
                <span class="muted">PDF, DOC, or DOCX up to 5MB</span>
            </div>
            <div class="upload-layout">
                <form id="ta-cv-form" class="cv-upload-form">
                    <input id="ta-cv-file-input" class="cv-upload-input" type="file" name="cvFile" accept=".pdf,.doc,.docx" />
                    <div id="ta-cv-dropzone" class="cv-dropzone" tabindex="0" role="button" aria-label="Drop CV file here or choose file">
                        <div class="cv-dropzone-inner">
                            <div class="cv-dropzone-title">Drop your CV here</div>
                            <div class="cv-dropzone-hint">Use this space for your latest academic CV or teaching support resume.</div>
                            <button id="ta-cv-pick-btn" class="glass-secondary-btn inline cv-pick-btn" type="button">Choose File</button>
                            <div id="ta-cv-selected-file" class="cv-dropzone-file">No file selected</div>
                        </div>
                    </div>
                    <div class="cv-upload-actions">
                        <button class="primary-btn" type="submit">Upload CV</button>
                        <button class="ghost-btn" type="button" id="ta-cv-remove">Remove</button>
                    </div>
                </form>
                <div class="cv-status-card" id="ta-cv-current"></div>
            </div>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
