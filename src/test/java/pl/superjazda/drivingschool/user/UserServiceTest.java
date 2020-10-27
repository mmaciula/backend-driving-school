package pl.superjazda.drivingschool.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.superjazda.drivingschool.course.Course;
import pl.superjazda.drivingschool.course.CourseDto;
import pl.superjazda.drivingschool.course.CourseRepository;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.role.Role;
import pl.superjazda.drivingschool.role.RoleRepository;
import pl.superjazda.drivingschool.role.RoleType;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundWhenCalledShowLoggedUserTest() {
        userService.showLoggedUser();
    }

    @Test
    public void shouldAssignUserToCourseTest() {
        User instructor = new User("instructor", "instructor@domain.com", "test123", "John", "Smith");
        Course course = new Course("Name", "Description", 1500, new Date(), 12, instructor);
        User student = new User("student", "stud@domain.com", "password", "Joe", "Doe");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(student));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        UserDto userDto = userService.assignUserToCourse(1L);

        assertTrue(course.getMembers() == 11);
        assertFalse(userDto.getCourses().isEmpty());
    }

    @Test
    public void shouldAssignNewRoleToUserTest() {
        User user = new User("student", "stud@domain.com", "password", "Joe", "Doe");
        Role role = new Role(RoleType.ROLE_ADMIN);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));

        UserDto userWithUpdatedRoles = userService.assignRoleToUser("ROLE_ADMIN", "student");

        assertTrue(userWithUpdatedRoles.getRoles().contains("ROLE_ADMIN"));
    }
}
