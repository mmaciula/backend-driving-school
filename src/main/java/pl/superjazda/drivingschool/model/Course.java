package pl.superjazda.drivingschool.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.superjazda.drivingschool.model.dto.NewCourseDto;

import javax.persistence.Column;
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
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User instructor;
    @Column
    private int members;

    public Course() { }

    public Course(NewCourseDto course, User instructor) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }
}
