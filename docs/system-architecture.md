# System Architecture

## Overview

The system follows a layered architecture to separate different responsibilities in the application.

The architecture includes three main layers:

1. User Interface Layer
2. Application Logic Layer
3. Data Storage Layer

---

## User Interface Layer

The User Interface layer is responsible for displaying information and receiving user input.

Example interfaces include:

- Login page
- Registration page
- TA dashboard
- Job list page
- Job application page
- MO dashboard
- Admin dashboard

---

## Application Logic Layer

The Application Logic layer processes user requests and handles system logic.

This layer includes several services:

### User Service

Responsible for:

- user registration
- user login
- profile management

### Job Service

Responsible for:

- posting jobs
- browsing jobs
- viewing job details

### Application Service

Responsible for:

- submitting job applications
- tracking application status

### Admin Service

Responsible for:

- viewing users
- monitoring workload
- managing applications

---

## Data Storage Layer

The system stores data using simple file formats instead of databases.

Example storage files:

- users.json
- jobs.json
- applications.json

This approach keeps the system simple and focuses on software engineering principles.