# Data Model

## Overview

The system contains three main data entities:

1. User
2. Job
3. Application

---

## User

The User entity stores information about system users.

Attributes:

- userId
- name
- email
- password
- role
- skills
- cvPath

Description:

The user can be a Teaching Assistant, Module Organiser, or Admin.

---

## Job

The Job entity stores information about available TA positions.

Attributes:

- jobId
- title
- moduleName
- description
- requiredSkills
- deadline

Description:

Each job is posted by a Module Organiser and can receive multiple applications.

---

## Application

The Application entity stores job application information.

Attributes:

- applicationId
- userId
- jobId
- status

Description:

This entity connects a Teaching Assistant with a specific job.