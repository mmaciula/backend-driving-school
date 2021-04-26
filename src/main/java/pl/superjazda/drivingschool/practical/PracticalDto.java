package pl.superjazda.drivingschool.practical;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.superjazda.drivingschool.course.CourseDto;
import pl.superjazda.drivingschool.user.UserDto;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PracticalDto {
    private Long id;
    private Date date;
    private int instructorRate;
    private String comment;
    private CourseDto course;
    private UserDto instructor;
    private UserDto student;

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
}
