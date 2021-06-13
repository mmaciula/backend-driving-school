package pl.superjazda.drivingschool.api.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private float cost;
    private Date startDate;
    private String instructorUsername;
    private String instructorName;
    private int members;

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
}
