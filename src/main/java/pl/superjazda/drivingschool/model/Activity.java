package pl.superjazda.drivingschool.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_generator")
    @SequenceGenerator(name = "activity_generator", sequenceName = "activity_sequence", initialValue = 2000001)
    private Long id;
    @DateTimeFormat
    private Date dateOfActivity;
    @Column
    private int rate;
    @Column
    private String comment;
    @ManyToOne
    private Course course;
    @ManyToOne
    private User instructor;
    @ManyToOne
    private User student;

    public Activity() { }

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
