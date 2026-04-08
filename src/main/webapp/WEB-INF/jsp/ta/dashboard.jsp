<%
    request.setAttribute("pageTitle", "TA Dashboard");
%>
<%@ include file="/WEB-INF/jsp/common/head.jspf" %>
<%@ include file="/WEB-INF/jsp/common/topbar.jspf" %>

<div class="workspace">
    <%@ include file="/WEB-INF/jsp/common/sidebar.jspf" %>
    <main class="content-area" id="ta-dashboard-page">
        <section class="hero-banner glass-panel reveal-up">
            <div class="hero-banner-copy">
                <span class="eyebrow eyebrow-subtle">TA Workspace</span>
                <h2>Welcome back, <span id="ta-name">TA User</span></h2>
                <p class="hero-banner-text">Track application progress, keep your profile ready, and stay ahead of role deadlines without losing the bigger picture.</p>
                <div class="hero-banner-meta">
                    <span class="meta-chip meta-chip--primary">Spring recruitment cycle</span>
                    <span class="meta-chip">Profile completeness directly affects shortlist quality</span>
                </div>
            </div>
            <aside class="hero-banner-side">
                <span class="section-kicker">Next best move</span>
                <strong class="hero-side-title" id="ta-hero-highlight">Review open roles this week</strong>
                <p class="hero-side-text" id="ta-hero-note">Keep your CV current and prioritise roles with stronger module fit.</p>
            </aside>
        </section>

        <section class="stats-grid stats-grid--ta">
            <article class="stat-card stat-card--featured glass-panel">
                <span class="stat-eyebrow">Opportunity coverage</span>
                <h3>Open Jobs</h3>
                <p id="ta-open-jobs">0</p>
                <span class="stat-note">Fresh roles currently visible in your workspace.</span>
            </article>
            <article class="stat-card glass-panel">
                <span class="stat-eyebrow">Pipeline</span>
                <h3>Submitted</h3>
                <p id="ta-submitted">0</p>
                <span class="stat-note">Applications already sent for review.</span>
            </article>
            <article class="stat-card glass-panel">
                <span class="stat-eyebrow">Awaiting review</span>
                <h3>Pending</h3>
                <p id="ta-pending">0</p>
                <span class="stat-note">Roles still waiting on module owner feedback.</span>
            </article>
            <article class="stat-card glass-panel">
                <span class="stat-eyebrow">Positive outcome</span>
                <h3>Selected</h3>
                <p id="ta-selected">0</p>
                <span class="stat-note">Offers or confirmed shortlist decisions.</span>
            </article>
        </section>

        <section class="dashboard-grid">
            <article class="panel module-panel module-panel--activity glass-panel">
                <div class="panel-head panel-head--rich">
                    <div>
                        <span class="section-kicker">Activity</span>
                        <h3>Latest Applications</h3>
                    </div>
                    <a class="text-link" href="${pageContext.request.contextPath}/pages/ta/applications">View all</a>
                </div>
                <p class="panel-description">Recent application movement, review decisions, and time-sensitive progress.</p>
                <div id="ta-latest-apps" class="list-stack"></div>
            </article>
            <article class="panel module-panel module-panel--recommend glass-panel">
                <div class="panel-head panel-head--rich">
                    <div>
                        <span class="section-kicker">Recommended</span>
                        <h3>Recommended Jobs</h3>
                    </div>
                    <a class="text-link" href="${pageContext.request.contextPath}/pages/ta/jobs">Explore jobs</a>
                </div>
                <p class="panel-description">Roles surfaced for visibility, module fit, and upcoming submission windows.</p>
                <div id="ta-recommended-jobs" class="list-stack"></div>
            </article>
        </section>
    </main>
</div>

<%@ include file="/WEB-INF/jsp/common/toast.jspf" %>
<%@ include file="/WEB-INF/jsp/common/modal.jspf" %>
<%@ include file="/WEB-INF/jsp/common/scripts.jspf" %>
