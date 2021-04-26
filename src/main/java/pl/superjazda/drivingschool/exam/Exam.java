package pl.superjazda.drivingschool.exam;

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
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_generator")
    @SequenceGenerator(name = "exam_generator", sequenceName = "exam_sequence", initialValue = 3000001)
    private Long id;
    @DateTimeFormat
    private Date examDate;
    @ManyToOne
    private User student;
    @ManyToOne
    private User instructor;
    @ManyToOne
    private Course course;
    private Boolean occupied;
    private Boolean passed;

    public Exam(Date examDate, Course course, User instructor) {
        this.examDate = examDate;
        this.student = null;
        this.instructor = instructor;
        this.course = course;
        this.passed = false;
        this.occupied = false;
    }

    public Exam(Date date, User student, Course course) {
        this.examDate = date;
        this.student = student;
        this.instructor = course.getInstructor();
        this.course = course;
        this.occupied = true;
        this.passed = false;
    }
}
