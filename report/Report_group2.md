# Brief Report: TA Recruitment System Requirement Analysis and Iteration Plan

## 1. Project Background & Scope
The BUPT International School recruits Teaching Assistants (TAs) every semester to support academic modules and various extracurricular activities. Currently, the administration and Module Organisers (MOs) rely heavily on manual procedures involving paper forms, email threads, and decentralized Excel spreadsheets. This legacy workflow is highly inefficient, prone to human error, and lacks transparency. It makes it exceedingly difficult for TAs to track their application statuses, for MOs to filter and select qualified candidates efficiently, and for Administrators to monitor overall workloads effectively.

To resolve these operational bottlenecks, our Agile development team is designing and implementing a digitized **TA Recruitment System**. The system is tailored to serve three primary stakeholders:
*   **Teaching Assistants (TAs):** To find jobs, manage profiles, upload CVs, and track applications.
*   **Module Organisers (MOs):** To post job vacancies, review applications, and select candidates.
*   **Administrators (Admin):** To maintain global oversight and ensure fair workload distribution among TAs.

**Project Scope:** The project focuses on digitalizing the core recruitment workflow. Following the mandatory module specifications, the system will be developed using Java (Stand-alone or Servlet/JSP) without the use of relational databases. All data persistence (input/output) will be handled via simple text file formats (e.g., CSV, JSON, or XML). The primary objective is to deliver a functional, modular, and extensible prototype that demonstrates the system's core value: streamlining recruitment and balancing workload.

## 2. Fact-finding Techniques
To ensure our software accurately addresses the customer's needs and to define the system's exact requirements, our team employed a combination of qualitative fact-finding techniques:

*   **Document Review:** We systematically analyzed the existing Excel templates, application forms, and CV submission guidelines currently used by the BUPT International School. This allowed us to map the data fields required for the system (e.g., applicant skills, availability, module requirements) and understand the legacy data flow.
*   **Interviews and Scenario Analysis:** We conducted structured mock interviews simulating conversations with MOs and Admins. We identified critical pain points: MOs struggle with comparing multiple CVs simultaneously, and Admins lack a unified dashboard to see if a specific TA is overloaded with too many modules. 
*   **Surveys (Simulated):** We evaluated the typical TA experience through scenario-based surveys. The feedback indicated that TAs desire a centralized platform to view all available positions and receive real-time updates on whether their applications are pending, accepted, or rejected.

These fact-finding activities provided the empirical foundation for our requirements engineering, ensuring that the system is designed to solve real-world administrative friction.

## 3. User Stories & Backlog Formation
Based on the insights gathered from the fact-finding phase, we defined our system's user roles and translated their needs into actionable Agile User Stories. By focusing on the "Who, What, and Why," we constructed a comprehensive Product Backlog.

The logic behind the backlog formation is categorized by user roles:
*   **TA-centric Stories:** Focus on usability and accessibility. For example, *"As a TA, I want to upload my CV and list my skills, so that MOs can evaluate my qualifications."* and *"As a TA, I want to view a list of open positions, so that I can apply for relevant modules."*
*   **MO-centric Stories:** Focus on efficiency and decision-making. For example, *"As an MO, I want to view the number of applicants for my posted jobs, so that I can manage the selection process."*
*   **Admin-centric Stories:** Focus on system governance. For example, *"As an Admin, I want to view the overall weekly hours of every TA, so that I can prevent workload imbalances (overloading)."*

Each User Story in the backlog is accompanied by explicit Acceptance Criteria to ensure the development team has a clear definition of "Done" for every feature.

## 4. Prioritisation & Estimation Methods
Given the strict project timeline and the necessity of incremental delivery, effective prioritisation and estimation were critical to our Sprint planning.

**Prioritisation Method (MoSCoW):**
We utilized the MoSCoW method to categorize the Product Backlog, ensuring the core value chain is established first:
*   **Must-Have:** Fundamental infrastructure including file-based data storage, user login/registration, TA applying for jobs, and MO selecting applicants. These form the minimal viable product (MVP).
*   **Should-Have:** Admin dashboard for workload tracking, dynamic application status updates, and UI refinements.
*   **Could-Have:** AI-assisted features (e.g., skill matching or missing skill identification) and advanced filtering options.
*   **Won't-Have (for now):** Complex database integrations (strictly prohibited by project specifications) and external email notifications.

**Estimation Method (Planning Poker):**
To estimate the effort and complexity of each User Story, the team employed **Planning Poker** using the **Fibonacci sequence** (1, 2, 3, 5, 8, 13, 21). 
During our Agile planning meetings, team members anonymously voted on the Story Points for each task. If discrepancies occurred (e.g., one member voted 3, another voted 13), the team discussed the technical risks—such as the complexity of file I/O operations versus simple UI rendering—until a consensus was reached. This method prevented anchoring bias and ensured realistic workload distribution among the 6 group members.

## 5. Iteration Planning (Revised)

To ensure structured and incremental development, the project adopts an Agile Scrum framework divided into four well-defined iterations. Each iteration is designed with a clear objective, aligned deliverables, and measurable outcomes, enabling continuous feedback and progressive refinement.

---

### Sprint 1 – Requirements & Design (Week 3)

This initial sprint focuses on establishing a solid foundation for the system. The team conducts requirement analysis through fact-finding techniques, defines user roles, and translates requirements into structured user stories. A complete Product Backlog is constructed, including prioritisation (MoSCoW) and estimation (Planning Poker).

In parallel, the system architecture is outlined and UI/UX prototypes are developed to visualise the system workflow and gather early feedback.

**Deliverables:**

* Product Backlog
* Prototype (low/medium fidelity)
* Brief Report

This sprint ensures a clear project scope and provides a roadmap for subsequent development.

---

### Sprint 2 – Core Implementation (Weeks 4–5)

This sprint focuses on building the Minimum Viable Product (MVP). The team implements the core system architecture using Java and establishes file-based data storage. Essential TA functionalities are developed, including user authentication, profile management, job browsing, and basic job application.

**Deliverables:**

* Working Software Version 1
* Core functional modules (TA workflow)

This stage produces the first functional version of the system, demonstrating the primary user journey.

---

### Sprint 3 – System Expansion (Weeks 6–8)

In this iteration, the system is extended to support Module Organiser (MO) and Administrator functionalities. Features such as job posting, applicant selection, application status tracking, and workload monitoring are implemented.

The team also improves usability, enhances UI consistency, and introduces validation and error handling mechanisms.

**Deliverables:**

* Working Software Version 2 & 3
* Complete multi-role functionality

This sprint ensures that all user roles are fully supported and that the system becomes functionally comprehensive.

---

### Sprint 4 – Testing & Finalisation (Weeks 9–11)

The final sprint focuses on system integration, testing, and delivery preparation. The team conducts acceptance testing, fixes identified issues, and refines performance and usability. Optional AI-assisted features may be explored if feasible.

Documentation, including JavaDocs, user manuals, and demonstration materials, is completed.

**Deliverables:**

* Final Software System
* Test programs and documentation
* Demonstration video

This iteration ensures the system is stable, complete, and ready for final evaluation.

---

Overall, this iterative approach ensures continuous progress, risk reduction, and alignment with Agile principles, while enabling the team to deliver a functional and well-structured software system.
