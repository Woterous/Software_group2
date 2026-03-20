# User Stories and Acceptance Criteria

This document is the story-level execution baseline for Sprint planning.

## 1. Story Format
Each story follows:
- As a `<role>`, I want `<capability>`, so that `<value>`.

Each story includes:
- priority (MoSCoW / P-level)
- target sprint
- acceptance criteria (testable)

## 2. Teaching Assistant (TA)
### US-TA-001: Account Registration
- Story: As a TA, I want to register an account, so that I can apply for TA jobs.
- Priority: Must (P1)
- Target Sprint: Sprint 2
- Acceptance Criteria:
  - user ID/email uniqueness is validated
  - required fields are mandatory
  - role is stored as TA
  - registration data is persisted in user storage file

### US-TA-002: Account Login
- Story: As a TA, I want to log in, so that I can access my account.
- Priority: Must (P1)
- Target Sprint: Sprint 2
- Acceptance Criteria:
  - credentials are validated against stored records
  - invalid login returns explicit error feedback
  - successful login redirects to TA dashboard

### US-TA-003: Profile Management
- Story: As a TA, I want to create and update my profile, so that MOs can evaluate my background.
- Priority: Must (P1)
- Target Sprint: Sprint 2
- Acceptance Criteria:
  - TA can create/edit profile attributes (skills, contact, major)
  - updates overwrite prior profile values consistently
  - profile is retrievable by MO during applicant review

### US-TA-004: CV Upload
- Story: As a TA, I want to upload my CV, so that I can present my qualifications.
- Priority: Must (P1)
- Target Sprint: Sprint 2
- Acceptance Criteria:
  - allowed file types are validated
  - CV path/reference is stored and linked to TA profile
  - TA can replace or remove a previously uploaded CV reference

### US-TA-005: Browse Jobs
- Story: As a TA, I want to browse available jobs, so that I can find suitable opportunities.
- Priority: Must (P1)
- Target Sprint: Sprint 2
- Acceptance Criteria:
  - open job list is loaded from storage
  - each item shows at least title, module, and deadline
  - list supports basic keyword filtering

### US-TA-006: View Job Details
- Story: As a TA, I want to view job details, so that I can understand requirements before applying.
- Priority: Must (P1)
- Target Sprint: Sprint 2
- Acceptance Criteria:
  - detail page displays requirements and description
  - job deadline is visible
  - page provides a clear action to apply

### US-TA-007: Apply for Job
- Story: As a TA, I want to apply for a job, so that I can become a TA.
- Priority: Must (P1)
- Target Sprint: Sprint 2
- Acceptance Criteria:
  - application record is created with user ID and job ID
  - initial status is set to Pending
  - duplicate applications for same TA-job pair are blocked

### US-TA-008: Track Application Status
- Story: As a TA, I want to check my application status, so that I know whether I was selected.
- Priority: Should (P2)
- Target Sprint: Sprint 3
- Acceptance Criteria:
  - TA sees current status for each submitted application
  - status options include Pending, Selected, Rejected
  - records are filtered to the logged-in TA only

## 3. Module Organiser (MO)
### US-MO-001: Post Job
- Story: As a MO, I want to post TA jobs, so that I can recruit teaching assistants.
- Priority: Must (P1)
- Target Sprint: Sprint 3
- Acceptance Criteria:
  - MO can create job with required fields
  - deadline validation prevents invalid posting
  - posted job appears in TA job list

### US-MO-002: View Applicants
- Story: As a MO, I want to view applicants, so that I can evaluate candidates.
- Priority: Must (P1)
- Target Sprint: Sprint 3
- Acceptance Criteria:
  - MO can filter applications by job
  - applicant profile and CV reference are accessible
  - list is synchronized with latest application status

### US-MO-003: Review Skills
- Story: As a MO, I want to review applicant skills, so that I can shortlist suitable candidates.
- Priority: Should (P2)
- Target Sprint: Sprint 3
- Acceptance Criteria:
  - applicant skills are visible beside job requirements
  - MO can record review notes
  - review information is saved for later selection decisions

### US-MO-004: Select Applicant
- Story: As a MO, I want to select applicants, so that I can assign TAs to my module.
- Priority: Must (P1)
- Target Sprint: Sprint 3
- Acceptance Criteria:
  - MO can update application status to Selected or Rejected
  - TA-facing status is updated consistently
  - selection rules prevent invalid over-assignment

## 4. Administrator (Admin)
### US-AD-001: View Users
- Story: As an Admin, I want to view all users, so that I can monitor system usage.
- Priority: Should (P2)
- Target Sprint: Sprint 3
- Acceptance Criteria:
  - user list includes all roles
  - list supports role-based filtering
  - data view is read-only for baseline scope

### US-AD-002: View Applications
- Story: As an Admin, I want to view all applications, so that I can monitor recruitment progress.
- Priority: Should (P2)
- Target Sprint: Sprint 3
- Acceptance Criteria:
  - Admin can view all application records
  - records can be filtered by status and job
  - records are linked to user and job references

### US-AD-003: Monitor Workload
- Story: As an Admin, I want to check TA workload, so that tasks are fairly distributed.
- Priority: Must (P1)
- Target Sprint: Sprint 3
- Acceptance Criteria:
  - workload metric is calculated from selected assignments
  - dashboard shows TA workload summary
  - list supports sorting by workload level
