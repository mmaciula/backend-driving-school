package pl.superjazda.drivingschool.model.dto;

import java.util.Date;

public class CourseDto {
    private String name;
    private String description;
    private float cost;
    private Date startDate;
    private String instructorUsername;
    private int members;

    public CourseDto() { }

    public CourseDto(String name, String description, float cost, Date startDate, String instructorUsername, int members) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.startDate = startDate;
        this.instructorUsername = instructorUsername;
        this.members = members;
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

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }
}
