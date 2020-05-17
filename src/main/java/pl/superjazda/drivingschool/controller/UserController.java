package pl.superjazda.drivingschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.exception.RoleNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.model.Course;
import pl.superjazda.drivingschool.model.Role;
import pl.superjazda.drivingschool.model.RoleType;
import pl.superjazda.drivingschool.model.User;
import pl.superjazda.drivingschool.model.dto.UserDto;
import pl.superjazda.drivingschool.repository.CourseRepository;
import pl.superjazda.drivingschool.repository.RoleRepository;
import pl.superjazda.drivingschool.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, CourseRepository courseRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/me")
    @PostAuthorize("returnObject.getUsername() == authentication.principal.username")
    public UserDto showLogInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userFromDatabase = userRepository.findByUsername(username);
        if (!userFromDatabase.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        UserDto user = new UserDto(userFromDatabase.get());

        return user;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserDto> users = new ArrayList<>();

        userRepository.findAll().forEach(user -> {
            if (user.getUsername() != getCurrentUser().getUsername()) {
                users.add(new UserDto(user));
            }
        });
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/course/add/{id}")
    public ResponseEntity<?> assignCourseToUser(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findUserByUsername(username);

        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException("Course not found"));

        List<Course> userCourses = user.getCourses();
        userCourses.add(course);
        user.setCourses(userCourses);

        updateParticipants(id);

        userRepository.save(user);

        return ResponseEntity.ok(new UserDto(user));
    }

    @PostMapping("/roles/assign/{role}/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRoleToUser(@PathVariable String role, @PathVariable String username) {
        User user = findUserByUsername(username);
        RoleType roleType = findRole(role);
        Role roleFromDatabase = roleRepository.findByName(roleType).orElseThrow(() -> new RoleNotFoundException("Role not found"));

        Set<Role> userRoles = user.getRoles();
        userRoles.add(roleFromDatabase);
        user.setRoles(userRoles);
        userRepository.save(user);

        return ResponseEntity.ok(new UserDto(user));
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        User user = findUserByUsername(username);
        UserDto userDto = new UserDto(user);
        userRepository.delete(user);

        return ResponseEntity.ok(userDto);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = findUserByUsername(username);
        return user;
    }

    private User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user;
    }

    private void updateParticipants(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException("Course not found"));

        int participants = course.getMembers();
        participants = participants -1;

        course.setMembers(participants);
        courseRepository.save(course);
    }

    private RoleType findRole(String name) {
        RoleType roleType = null;
        switch (name) {
            case "ROLE_USER":
                roleType = RoleType.ROLE_USER;
                break;
            case "ROLE_ADMIN":
                roleType = RoleType.ROLE_ADMIN;
                break;
            case "ROLE_MODERATOR":
                roleType = RoleType.ROLE_MODERATOR;
                break;
        }
        return roleType;
    }
}
