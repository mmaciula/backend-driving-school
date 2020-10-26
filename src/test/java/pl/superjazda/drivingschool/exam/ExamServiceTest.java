package pl.superjazda.drivingschool.exam;

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
import pl.superjazda.drivingschool.exception.ExamNotFoundException;
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
public class ExamServiceTest {
    @Mock
    private ExamRepository examRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private ExamService examService;

    @Before
    public void setUp() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
    }

    @Test
    public void shouldAddNewExamTest() {
        Course course = new Course("Course name", "Course description", 1000, new Date(), 7, new User());
        AddExam addExam = new AddExam(new Date());
        User user = new User("username", "user@domain.com", "pass123", "Joe", "Doe");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        ExamDto examDto = examService.addExam(1L, addExam);

        assertTrue(examDto.getStudentUsername().equals(user.getUsername()));
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionWhenUserIsNotFoundTest() {
        ExamDto examDto = examService.addExam(1L, new AddExam(new Date()));
    }

    @Test(expected = CourseNotFoundException.class)
    public void shouldThrowExceptionWhenCourseIsNotFoundTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        ExamDto examDto = examService.addExam(1L, new AddExam(new Date()));
    }

    @Test
    public void shouldSignForExamTest() {
        Exam exam = new Exam(new Date(), new Course(), new User());
        User user = new User();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(examRepository.findById(anyLong())).thenReturn(Optional.of(exam));

        ExamDto dto = examService.signInForExam(1L);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundWhileTryingToSignInForExamTest() {
        ExamDto examDto = examService.signInForExam(1L);
    }

    @Test(expected = ExamNotFoundException.class)
    public void shouldFindExamNotFoundWhenExamDoesNotExistTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        ExamDto examDto = examService.signInForExam(1L);
    }
}
