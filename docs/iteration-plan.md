# Sprint Plan (Iteration Plan)

## 1. Planning Principles
This plan follows the course handout and enforces:
- incremental delivery by sprint
- clear sprint objectives and exit criteria
- measurable artefacts at each assessment checkpoint

Terminology rule:
- `Sprint` and `Iteration` refer to the same unit in this project
- this document uses `Sprint` as the primary term

## 2. Timeline and Milestones
| Sprint | Date Window | Checkpoint |
|---|---|---|
| Sprint 1 | March 16, 2026 - March 22, 2026 | Assessment 1 (30%) |
| Sprint 2 | March 23, 2026 - April 12, 2026 | Intermediate checkpoint (20%) |
| Sprint 3 | April 13, 2026 - May 3, 2026 | Internal integration gate |
| Sprint 4 | May 4, 2026 - May 17, 2026 | Final readiness gate |
| Final Submission | By May 24, 2026 | Final Assessment (50%) |

## 3. Sprint Objectives and Outputs
### Sprint 1: Requirements and Design
Objective:
- establish requirement baseline, backlog structure, and architecture direction

Primary outputs:
- product backlog (`ProductBacklog_group2.xlsx`)
- prototype package (source images, export-ready for PDF submission)
- sprint brief report
- requirement, user story, architecture, and data-model documents

Exit criteria:
- requirements are complete and testable
- stories include acceptance criteria
- backlog priorities and estimates are coherent
- architecture and data model are aligned

### Sprint 2: Core MVP Implementation
Objective:
- implement minimum end-to-end TA workflow

Primary outputs:
- registration/login/profile/CV flow
- job browse/detail/apply flow
- baseline file persistence and validation

Exit criteria:
- TA core flow runs end-to-end
- no blocking defects on must-have stories
- checkpoint demo is executable

### Sprint 3: Role Expansion
Objective:
- add MO and Admin operational features

Primary outputs:
- MO posting and applicant decision flow
- TA status visibility
- Admin global view and workload monitoring

Exit criteria:
- all role workflows are functionally connected
- cross-role data consistency is verified

### Sprint 4: Quality and Delivery
Objective:
- stabilize system and complete final packaging

Primary outputs:
- test evidence and bug-fix log
- user-facing documentation and JavaDoc
- final demo script and rehearsal package

Exit criteria:
- critical defects are closed
- final deliverables meet handout submission checklist

## 4. Delivery Risk Controls
### Risk 1: Requirement drift
- Control: requirement-story-backlog traceability matrix maintained each sprint

### Risk 2: Overcommitment in Sprint 2
- Control: lock Sprint 2 scope to Must stories only

### Risk 3: Integration late in project
- Control: enforce cross-role integration checks at Sprint 3 gate

### Risk 4: Assessment evidence gaps
- Control: maintain artefact checklist with owner and due date

## 5. Collaboration and Git Evidence Rules
To satisfy assessment evidence requirements:
- each member works on a personal branch
- changes are merged through pull requests
- issues are used to track work items and decisions
- commits must map to backlog/story IDs where possible

Minimum sprint evidence expected:
- branch activity from all members
- merged PR history for sprint scope
- updated README/report when scope or decisions change

## 6. Definition of Done (Project Level)
A sprint item is done only when:
- functionality and acceptance criteria are both satisfied
- related documentation is updated
- storage impact is reflected in data model
- evidence is present for review/demo
