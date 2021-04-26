package pl.superjazda.drivingschool.course;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.superjazda.drivingschool.user.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_generator")
    @SequenceGenerator(name = "course_generator", sequenceName = "course_sequence", initialValue = 1000001)
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String description;
    @DecimalMin("1")
    @DecimalMax("5000")
    private float cost;
    private Date startDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User instructor;
    private int members;

    public Course(AddCourse course, User instructor) {
       this.name = course.getName();
       this.description = course.getDescription();
       this.cost = course.getCost();
       this.startDate = course.getStartDate();
       this.members = course.getMembers();
       this.instructor = instructor;
    }

    public Course(String name, String description, float cost, Date startDate, int members, User instructor) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.instructor = instructor;
        this.startDate = startDate;
        this.members = members;
    }
}
