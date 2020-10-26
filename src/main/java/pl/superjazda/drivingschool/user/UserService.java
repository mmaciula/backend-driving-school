package pl.superjazda.drivingschool.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

@Service
public class UserService {
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, CourseRepository courseRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto showLoggedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userFromDatabase = userRepository.findByUsername(username);
        if (!userFromDatabase.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        UserDto user = new UserDto(userFromDatabase.get());

        return user;
    }

    public List<UserDto> findAllRegisteredUsers() {
        List<UserDto> users = new ArrayList<>();

        userRepository.findAll().forEach(user -> {
            if (user.getUsername() != getCurrentUser().getUsername()) {
                users.add(new UserDto(user));
            }
        });

        return users;
    }

    public UserDto assignUserToCourse(Long courseId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }

        Set<Course> userCourses = user.get().getCourses();
        userCourses.add(course.get());
        user.get().setCourses(userCourses);

        Optional<Course> courseToUpdateParticipants = courseRepository.findById(courseId);
        if (!courseToUpdateParticipants.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }
        int participants = courseToUpdateParticipants.get().getMembers();
        participants = participants -1;
        courseToUpdateParticipants.get().setMembers(participants);
        courseRepository.save(courseToUpdateParticipants.get());

        userRepository.save(user.get());

        return new UserDto(user.get());
    }

    public UserDto assignRoleToUser(String role, String username) {
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

        return new UserDto(user.get());
    }

    public UserDto deleteUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        UserDto userDto = new UserDto(user.get());
        userRepository.delete(user.get());

        return userDto;
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
