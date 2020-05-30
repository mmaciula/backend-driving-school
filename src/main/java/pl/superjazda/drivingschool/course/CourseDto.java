package pl.superjazda.drivingschool.course;

import java.util.Date;

public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private float cost;
    private Date startDate;
    private String instructorUsername;
    private String instructorName;
    private int members;

    public CourseDto() { }

    public CourseDto(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.description = course.getDescription();
        this.cost = course.getCost();
        this.startDate = course.getStartDate();
        this.members = course.getMembers();
        if (course.getInstructor() != null) {
            this.instructorUsername = course.getInstructor().getUsername();
            this.instructorName = course.getInstructor().getName() + " " + course.getInstructor().getSurname();
        } else {
            this.instructorName = "";
            this.instructorUsername = "";
        }
    }

    public CourseDto(Long id, String name, String description, float cost, Date startDate, String instructorUsername, String instructorName, int members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.startDate = startDate;
        this.instructorUsername = instructorUsername;
        this.instructorName = instructorName;
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getInstructorUsername() {
        return instructorUsername;
    }

    public void setInstructorUsername(String instructorUsername) {
        this.instructorUsername = instructorUsername;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }
}
