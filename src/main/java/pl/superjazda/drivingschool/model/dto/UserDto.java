package pl.superjazda.drivingschool.model.dto;

import pl.superjazda.drivingschool.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDto {
    private String username;
    private String email;
    private String name;
    private String surname;
    private Set<String> roles;
    private List<CourseDto> courses;

    public UserDto() { }

    public UserDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        Set<String> userRoles = new HashSet<>();
        user.getRoles().forEach(role -> {
            userRoles.add(role.getRoleType().name());
        });
        this.roles = userRoles;
        List<CourseDto> userCourses = new ArrayList<>();
        user.getCourses().forEach(course -> {
            userCourses.add(new CourseDto(course));
        });
        this.courses = userCourses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public List<CourseDto> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseDto> courses) {
        this.courses = courses;
    }
}
