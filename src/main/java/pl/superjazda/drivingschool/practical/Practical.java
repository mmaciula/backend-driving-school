package pl.superjazda.drivingschool.practical;

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

    public Practical() { }

    public Practical(Date date, Course course, User instructor) {
        this.date = date;
        this.course = course;
        this.instructor = instructor;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
