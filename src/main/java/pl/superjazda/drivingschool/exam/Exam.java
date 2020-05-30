package pl.superjazda.drivingschool.exam;

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

    public Exam() {
    }

    public Exam(Date examDate, Course course, User instructor) {
        this.examDate = examDate;
        this.student = null;
        this.instructor = instructor;
        this.course = course;
        this.passed = false;
        this.occupied = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }
}
