package model;

public class Job {
    private String title;
    private String courseName;
    private String deadline;
    private String requirements;
    private String description;

    public Job(String title, String courseName, String deadline, String requirements, String description) {
        this.title = title;
        this.courseName = courseName;
        this.deadline = deadline;
        this.requirements = requirements;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getDescription() {
        return description;
    }
}
