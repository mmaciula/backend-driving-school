package pl.superjazda.drivingschool.model.dto;

import pl.superjazda.drivingschool.model.Activity;

import java.util.Date;

public class ActivityDto {
    private Long id;
    private Date dateOfActivity;
    private int rate;
    private String comment;
    private CourseDto course;
    private UserDto instructor;
    private UserDto student;

    public ActivityDto(Long id, Date dateOfActivity, int rate, String comment, CourseDto course, UserDto instructor, UserDto student) {
        this.id = id;
        this.dateOfActivity = dateOfActivity;
        this.rate = rate;
        this.comment = comment;
        this.course = course;
        this.instructor = instructor;
        this.student = student;
    }

    public ActivityDto(Activity activity) {
        this.id = activity.getId();
        this.dateOfActivity = activity.getDateOfActivity();
        this.rate = activity.getRate();
        this.comment = activity.getComment();
        this.course = new CourseDto(activity.getCourse());
        this.instructor = new UserDto(activity.getInstructor());
        if (activity.getStudent() == null) {
            this.student = null;
        } else {
            this.student = new UserDto(activity.getStudent());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfActivity() {
        return dateOfActivity;
    }

    public void setDateOfActivity(Date dateOfActivity) {
        this.dateOfActivity = dateOfActivity;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }

    public UserDto getInstructor() {
        return instructor;
    }

    public void setInstructor(UserDto instructor) {
        this.instructor = instructor;
    }

    public UserDto getStudent() {
        return student;
    }

    public void setStudent(UserDto student) {
        this.student = student;
    }
}
