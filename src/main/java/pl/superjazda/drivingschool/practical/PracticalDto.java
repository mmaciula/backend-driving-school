package pl.superjazda.drivingschool.practical;

import pl.superjazda.drivingschool.course.CourseDto;
import pl.superjazda.drivingschool.user.UserDto;

import java.util.Date;

public class PracticalDto {
    private Long id;
    private Date date;
    private int instructorRate;
    private String comment;
    private CourseDto course;
    private UserDto instructor;
    private UserDto student;

    public PracticalDto(Long id, Date date, int instructorRate, String comment, CourseDto course, UserDto instructor, UserDto student) {
        this.id = id;
        this.date = date;
        this.instructorRate = instructorRate;
        this.comment = comment;
        this.course = course;
        this.instructor = instructor;
        this.student = student;
    }

    public PracticalDto(Practical practical) {
        this.id = practical.getId();
        this.date = practical.getDate();
        this.instructorRate = practical.getInstructorRate();
        this.comment = practical.getComment();
        this.course = new CourseDto(practical.getCourse());
        this.instructor = new UserDto(practical.getInstructor());
        if (practical.getStudent() == null) {
            this.student = null;
        } else {
            this.student = new UserDto(practical.getStudent());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getInstructorRate() {
        return instructorRate;
    }

    public void setInstructorRate(int instructorRate) {
        this.instructorRate = instructorRate;
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
