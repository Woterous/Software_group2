# System Architecture

## 1. Architecture Goal
The architecture is designed to satisfy three constraints from the coursework:
- simple enough for iterative delivery
- modular enough for team parallel work
- extensible enough for Sprint 3/4 feature growth

## 2. Layered Design
The system uses a 3-layer architecture.

### Layer 1: User Interface Layer
Responsibilities:
- render role-based screens
- collect and validate input format
- invoke service operations

Representative screens:
- Login / Register
- TA Dashboard / Job List / Job Detail
- MO Dashboard / Applicant Review
- Admin Dashboard / Workload View

### Layer 2: Application Service Layer
Responsibilities:
- enforce business rules
- orchestrate cross-entity operations
- expose use-case-level methods to UI layer

Core services:
- `UserService`: register, authenticate, profile operations
- `JobService`: publish, list, and detail retrieval
- `ApplicationService`: apply, status transition, history retrieval
- `AdminService` (or equivalent module): global views and workload statistics

### Layer 3: File Storage Layer
Responsibilities:
- read and write structured files
- maintain entity-level persistence consistency
- provide storage abstraction to service layer

Default storage baseline:
- `users.json`
- `jobs.json`
- `applications.json`

## 3. Technology Decision
Implementation option selected for current baseline:
- Java stand-alone implementation path (course-compliant)

Alternative acceptable path:
- Java Web (Servlet/JSP) with the same service and storage boundary

Non-negotiable constraint:
- no relational database

## 4. Key Design Trade-Offs
### Trade-Off A: Simplicity vs Query Efficiency
- Choice: file-based persistence
- Benefit: low setup cost, high compliance with coursework constraints
- Cost: weaker query performance than database-backed systems

### Trade-Off B: Fast UI Iteration vs Full Service Completion
- Choice: role-first UI decomposition for Sprint progression
- Benefit: visible sprint outputs and stakeholder feedback earlier
- Cost: requires strict service contract discipline to avoid UI-business coupling

### Trade-Off C: Flexibility vs Strict Schema
- Choice: text-based files with controlled field definitions
- Benefit: easy portability and inspection
- Cost: additional validation logic is needed in services

## 5. Extension Direction
The architecture reserves extension points for optional features:
- skill matching module
- skill gap analysis
- workload balancing recommendation

These are add-on modules and must not break the core 3-layer separation.
