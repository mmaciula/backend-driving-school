package pl.superjazda.drivingschool.user;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.superjazda.drivingschool.course.Course;
import pl.superjazda.drivingschool.course.CourseRepository;
import pl.superjazda.drivingschool.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.exception.RoleNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.role.Role;
import pl.superjazda.drivingschool.role.RoleRepository;
import pl.superjazda.drivingschool.role.RoleType;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        // TODO create class to initialize test data: initUser(), initCourse(), etc.
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundWhenCalledShowLoggedUserTest() {
        userService.showLoggedUser();

        verify(userRepository).findByUsername("username");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldShowLoggedUserTest() {
        when(userRepository.findByUsername("username")).thenReturn(initUser());

        UserDto user = userService.showLoggedUser();

        assertTrue(user.getUsername().equals("student"));
        verify(userRepository).findByUsername("username");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldAssignUserToCourseTest() {
        Course course = initCourse();

        when(userRepository.findByUsername(anyString())).thenReturn(initUser());
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        UserDto userDto = userService.assignUserToCourse(1L);

        assertTrue(course.getMembers() == 11);
        assertFalse(userDto.getCourses().isEmpty());
    }

    @Test
    public void shouldThrowUserNotFoundWhileAssignUserToCourseTest() {
        exceptionRule.expect(UserNotFoundException.class);
        exceptionRule.expectMessage("User not found");

        userService.assignUserToCourse(1L);

        verify(userRepository).findByUsername("username");
        verifyNoInteractions(courseRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldThrowCourseNotFindWhileAssignUserToCourseTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(initUser());
        exceptionRule.expect(CourseNotFoundException.class);
        exceptionRule.expectMessage("Course not found");

        userService.assignUserToCourse(1L);

        verify(userRepository).findByUsername("username");
        verify(courseRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
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

    @Test
    public void shouldThrowUserNotFoundWhenAssignRoleToUserTest() {
        exceptionRule.expect(UserNotFoundException.class);
        exceptionRule.expectMessage("User not found");

        userService.assignRoleToUser("ROLE_ADMIN", "username");
    }

    @Test
    public void shouldThrowRoleNotFoundWhenAssignRoleToUserTest() {
        exceptionRule.expect(RoleNotFoundException.class);
        exceptionRule.expectMessage("Role not found");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(new User()));

        userService.assignRoleToUser("ROLE_TEST", "username");
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundWhileDeleteUserTest() {
        userService.deleteUser("username");

        verify(userRepository).findByUsername("username");
        verifyNoMoreInteractions(userRepository);
    }

    private Optional<User> initUser() {
        return Optional.of(new User("student", "stud@domain.com", "password", "Joe", "Doe"));
    }

    private User initInstructor() {
        return new User("instructor", "instructor@domain.com", "test123", "John", "Smith");
    }

    private Course initCourse() {
        return new Course("Name", "Description", 1500, new Date(), 12, initInstructor());
    }
}
