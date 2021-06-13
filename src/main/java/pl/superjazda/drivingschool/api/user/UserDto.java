package pl.superjazda.drivingschool.api.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.superjazda.drivingschool.api.course.CourseDto;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private String name;
    private String surname;
    private Set<String> roles;
    private Set<CourseDto> courses;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        Set<String> userRoles = new HashSet<>();
        user.getRoles().forEach(role -> {
            userRoles.add(role.getName().name());
        });
        this.roles = userRoles;
        Set<CourseDto> userCourses = new HashSet<>();
        user.getCourses().forEach(course -> {
            userCourses.add(new CourseDto(course));
        });
        this.courses = userCourses;
    }
}
