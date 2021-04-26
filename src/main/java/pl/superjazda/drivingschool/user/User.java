package pl.superjazda.drivingschool.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.superjazda.drivingschool.course.Course;
import pl.superjazda.drivingschool.practical.Practical;
import pl.superjazda.drivingschool.role.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq", initialValue = 11)
    private Long id;
    @NotBlank
    @Size(min = 3, message = "Username must be at least 3 characters", max = 60)
    @Column(unique = true)
    private String username;
    @NotBlank
    @Email(message = "Invalid email")
    private String email;
    @NotBlank
    @Size(min = 6, message = "Password is too short")
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_courses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();
    @OneToMany(mappedBy = "instructor")
    private Set<Course> instructedCourses;
    @OneToMany(mappedBy = "student")
    private Set<Practical> practicals;

    public User(String username, String email, String password, String name, String surname) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }
}
