package pl.superjazda.drivingschool.practical;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.superjazda.drivingschool.course.Course;
import pl.superjazda.drivingschool.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "practicals")
@Getter
@Setter
@NoArgsConstructor
public class Practical {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "practical_generator")
    @SequenceGenerator(name = "practical_generator", sequenceName = "practical_sequence", initialValue = 2000001)
    private Long id;
    @DateTimeFormat
    private Date date;
    private int instructorRate;
    private String comment;
    @ManyToOne
    private Course course;
    @ManyToOne
    private User instructor;
    @ManyToOne
    private User student;

    public Practical(Date date, Course course, User instructor) {
        this.date = date;
        this.course = course;
        this.instructor = instructor;
    }
}
