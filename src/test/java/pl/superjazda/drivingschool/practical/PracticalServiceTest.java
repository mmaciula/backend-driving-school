package pl.superjazda.drivingschool.practical;

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
import pl.superjazda.drivingschool.course.CourseRepository;
import pl.superjazda.drivingschool.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.exception.PracticalNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PracticalServiceTest {
    @Mock
    private PracticalRepository practicalRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private PracticalService practicalService;

    @Before
    public void setUp() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowInstructorMotFoundWhileTyringToCreateNewPracticalTest() {
        AddPractical addPractical = new AddPractical(new Date());

        PracticalDto practical = practicalService.createNewPractical(addPractical, 1L);
    }

    @Test(expected = CourseNotFoundException.class)
    public void shouldThrowCourseNotFoundWhileCreatingNewPractical() {
        AddPractical addPractical = new AddPractical(new Date());
        User instructor = new User();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(instructor));

        PracticalDto practical = practicalService.createNewPractical(addPractical, 1L);
    }

    @Test
    public void shouldSignUserToPractical() {
        User user = new User("Username", "user@domain.com", "password", "Joe", "Doe");
        Practical practical = new Practical(new Date(), new Course(), new User());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(practicalRepository.findById(anyLong())).thenReturn(Optional.of(practical));

        PracticalDto practicalDto = practicalService.signUserForPractical(1L);

        assertTrue((practicalDto.getStudent().getName()).equals(user.getName()));
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundWhileTryingToSignUserToPracticalTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        practicalService.signUserForPractical(1L);
    }

    @Test(expected = PracticalNotFoundException.class)
    public void shouldThrowPracticalNotFoundWhileTryingToSignUserToPracticalTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(practicalRepository.findById(anyLong())).thenReturn(Optional.empty());

        practicalService.signUserForPractical(1L);
    }

    @Test(expected = PracticalNotFoundException.class)
    public void shouldThrowPracticalNotFoundWhileRatingPracticalTest() {
        when(practicalRepository.findById(anyLong())).thenReturn(Optional.empty());

        practicalService.ratePractical(1L, 5);
    }

    @Test(expected = PracticalNotFoundException.class)
    public void shouldThrowPracticalNotFoundWhileCommentingPracticalTest() {
        when(practicalRepository.findById(1L)).thenReturn(Optional.empty());

        practicalService.commentPractical(1L, "Comment about practical");
    }
}
