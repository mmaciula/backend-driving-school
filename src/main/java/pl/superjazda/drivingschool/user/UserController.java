package pl.superjazda.drivingschool.user;

import org.springframework.beans.factory.annotation.Autowired;
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
import pl.superjazda.drivingschool.course.Course;
import pl.superjazda.drivingschool.course.CourseRepository;
import pl.superjazda.drivingschool.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.exception.RoleNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.role.Role;
import pl.superjazda.drivingschool.role.RoleRepository;
import pl.superjazda.drivingschool.role.RoleType;

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
    public ResponseEntity<?> findAllUsers() {
        List<UserDto> users = new ArrayList<>();

        userRepository.findAll().forEach(user -> {
            if (user.getUsername() != getCurrentUser().getUsername()) {
                users.add(new UserDto(user));
            }
        });
        return ResponseEntity.ok(users);
    }

    @PutMapping("/course/add/{id}")
    public ResponseEntity<?> assignCourseToUser(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }

        Set<Course> userCourses = user.get().getCourses();
        userCourses.add(course.get());
        user.get().setCourses(userCourses);

        Optional<Course> courseToUpdateParticipants = courseRepository.findById(id);
        if (!courseToUpdateParticipants.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }
        int participants = courseToUpdateParticipants.get().getMembers();
        participants = participants -1;
        courseToUpdateParticipants.get().setMembers(participants);
        courseRepository.save(courseToUpdateParticipants.get());

        userRepository.save(user.get());

        return ResponseEntity.ok(new UserDto(user.get()));
    }

    @PostMapping("/roles/assign/{role}/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRoleToUser(@PathVariable String role, @PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        RoleType roleType = findRoleByName(role);
        Optional<Role> roleFromDatabase = roleRepository.findByName(roleType);
        if (!roleFromDatabase.isPresent()) {
            throw new RoleNotFoundException("Role not found");
        }

        Set<Role> userRoles = user.get().getRoles();
        userRoles.add(roleFromDatabase.get());
        user.get().setRoles(userRoles);
        userRepository.save(user.get());

        return ResponseEntity.ok(new UserDto(user.get()));
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        UserDto userDto = new UserDto(user.get());
        userRepository.delete(user.get());

        return ResponseEntity.ok(userDto);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        return user.get();
    }

    private RoleType findRoleByName(String name) {
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
