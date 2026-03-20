# Data Model

## 1. Overview
The Sprint baseline data model contains three core entities:
- User
- Job
- Application

The model is designed for file-based persistence and direct alignment with user stories and backlog fields.

## 2. Entity Definitions
### 2.1 User
Storage file: `users.json`

| Field | Type | Required | Notes |
|---|---|---|---|
| `userId` | String | Yes | Unique identifier |
| `name` | String | Yes | Display name |
| `email` | String | Yes | Unique login/contact key |
| `password` | String | Yes | Stored securely in implementation phase |
| `role` | Enum | Yes | `TA`, `MO`, `ADMIN` |
| `skills` | List/String | Optional | Used for review/matching |
| `cvPath` | String | Optional | File reference for uploaded CV |

### 2.2 Job
Storage file: `jobs.json`

| Field | Type | Required | Notes |
|---|---|---|---|
| `jobId` | String | Yes | Unique identifier |
| `title` | String | Yes | Job title |
| `moduleName` | String | Yes | Module/course reference |
| `description` | String | Yes | Job summary |
| `requiredSkills` | List/String | Yes | Candidate screening basis |
| `deadline` | Date | Yes | Application deadline |
| `postedBy` | String | Optional | MO reference (`userId`) |

### 2.3 Application
Storage file: `applications.json`

| Field | Type | Required | Notes |
|---|---|---|---|
| `applicationId` | String | Yes | Unique identifier |
| `userId` | String | Yes | TA reference |
| `jobId` | String | Yes | Job reference |
| `status` | Enum | Yes | `Pending`, `Selected`, `Rejected` |
| `reviewNote` | String | Optional | MO/Admin note |
| `createdAt` | DateTime | Optional | Submission timestamp |
| `updatedAt` | DateTime | Optional | Last status update timestamp |

## 3. Relationship Model
- One User (TA) can submit many Applications
- One Job can receive many Applications
- Each Application links exactly one TA (`userId`) and one Job (`jobId`)

Cardinality summary:
- `User 1..* Application`
- `Job 1..* Application`

## 4. Data Integrity Rules
- `userId` and `email` must be unique in User records
- `jobId` must be unique in Job records
- `(userId, jobId)` pair must be unique in Application records
- `status` transitions must follow workflow rules (`Pending -> Selected/Rejected`)
- orphan records are invalid (application must reference existing user and job)

## 5. Model Evolution Policy
To keep sprint continuity:
- new fields must be backward-compatible
- required-field changes must include migration note
- any model change must update:
  - requirement-analysis
  - user-stories acceptance criteria
  - product backlog notes
