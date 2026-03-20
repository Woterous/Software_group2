# API Contracts

## 1. Global Conventions
- Base path: `/api/v1`
- Content type: `application/json`
- Auth: Session Cookie
- Standard query params:
  - `page`, `size`, `sort`, `order`, `keyword`
  - domain filters: `status`, `module`, `role`, `dateFrom`, `dateTo`

---

## 2. Auth Endpoints

### POST `/auth/register`
Create a user account.

Request body:
```json
{
  "name": "Alex Morgan",
  "email": "alex@school.edu",
  "password": "Password123!",
  "role": "ta",
  "skills": "Java, SQL",
  "cvPath": "/uploads/alex_cv.pdf"
}
```

Success response (`201`):
```json
{
  "success": true,
  "data": {
    "user": {
      "userId": "TA011",
      "name": "Alex Morgan",
      "email": "alex@school.edu",
      "role": "ta"
    }
  },
  "meta": null,
  "error": null
}
```

### POST `/auth/login`
Create session.

Request body:
```json
{
  "email": "emma@school.edu",
  "password": "Password123!",
  "role": "ta"
}
```

Success response (`200`):
```json
{
  "success": true,
  "data": {
    "user": {
      "userId": "TA001",
      "name": "Emma Thompson",
      "role": "ta"
    }
  },
  "meta": null,
  "error": null
}
```

### DELETE `/auth/logout`
Destroy session.

### GET `/auth/me`
Get current session user.

---

## 3. TA Endpoints

### GET `/ta/dashboard`
Return summary metrics and recent records.

Response data fields:
- `openJobs`
- `submitted`
- `pending`
- `selected`
- `latestApplications[]`
- `recommendedJobs[]`

### GET `/ta/profile`
Return profile for current TA.

### PUT `/ta/profile`
Update profile attributes.

Request body:
```json
{
  "name": "Emma Thompson",
  "email": "emma@school.edu",
  "skills": "Java, OOP, Tutoring",
  "major": "Software Engineering",
  "contact": "+86-13800010001"
}
```

### POST `/ta/profile/cv`
Upsert CV path reference.

Request body:
```json
{ "cvPath": "/uploads/emma_cv_v2.pdf" }
```

### DELETE `/ta/profile/cv`
Remove CV path reference.

### GET `/ta/jobs`
List jobs with filtering and pagination.

Query params:
- `page`, `size`
- `keyword`
- `module`
- `status`

Success response (`200`):
```json
{
  "success": true,
  "data": {
    "jobs": [
      {
        "jobId": "JOB001",
        "title": "TA for Software Engineering",
        "moduleName": "EBU6304",
        "status": "open"
      }
    ]
  },
  "meta": {
    "page": 1,
    "size": 6,
    "totalItems": 24,
    "totalPages": 4
  },
  "error": null
}
```

### GET `/ta/jobs/{jobId}`
Get job detail.

### POST `/ta/applications`
Submit application.

Request body:
```json
{
  "jobId": "JOB001"
}
```

Success response (`201`):
```json
{
  "success": true,
  "data": {
    "application": {
      "applicationId": "APP010",
      "jobId": "JOB001",
      "status": "pending"
    }
  },
  "meta": null,
  "error": null
}
```

### GET `/ta/applications`
List current TA applications.

Query params:
- `status`
- `keyword`

---

## 4. MO Endpoints

### GET `/mo/dashboard`
Return MO summary metrics and near-deadline jobs.

### GET `/mo/jobs`
List MO-owned jobs.

Query params:
- `status`
- `keyword`

### POST `/mo/jobs`
Create job.

### PUT `/mo/jobs/{jobId}`
Update job.

### GET `/mo/applicants`
List applicants with joined TA/job data.

Query params:
- `jobId`
- `status`
- `keyword`

### GET `/mo/review/{applicationId}`
Load review context for one application.

### PUT `/mo/applications/{applicationId}/status`
Update application status.

Request body:
```json
{
  "status": "selected",
  "reviewNote": "Strong technical fit"
}
```

---

## 5. Admin Endpoints

### GET `/admin/dashboard`
Return global metrics, recent applications, workload alerts.

### GET `/admin/users`
List all users (read-only).

Query params:
- `page`, `size`
- `role`
- `keyword`

### GET `/admin/applications`
List all applications.

Query params:
- `status`
- `module`
- `keyword`

### GET `/admin/workload`
Return workload summary rows.

Query params:
- `riskLevel` (`normal`, `warning`, `overload`)

---

## 6. Idempotency and Conflict Rules
- `POST /ta/applications` is not idempotent and returns `409` when duplicate.
- `PUT /mo/applications/{applicationId}/status` is idempotent for same status payload.
- `POST /auth/register` returns `409` when email already exists.

## 7. Frontend Adapter Alignment
`src/main/webapp/static/js/core/api-client.js` method names map 1:1 to this contract.
No backend endpoint should be renamed without updating:
- this file
- `openapi.yaml`
- `docs/api/INDEX.md`
