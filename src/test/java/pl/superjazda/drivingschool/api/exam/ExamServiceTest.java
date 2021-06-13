package pl.superjazda.drivingschool.api.exam;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.superjazda.drivingschool.api.course.Course;
import pl.superjazda.drivingschool.api.course.CourseRepository;
import pl.superjazda.drivingschool.web.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.web.exception.ExamNotFoundException;
import pl.superjazda.drivingschool.web.exception.UserNotFoundException;
import pl.superjazda.drivingschool.api.user.User;
import pl.superjazda.drivingschool.api.user.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
        AddExam addExam = new AddExam(new Date());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(initUser()));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(initCourse()));

        ExamDto examDto = examService.addExam(1L, addExam);

        assertTrue(examDto.getStudentUsername().equals("username"));
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionWhenUserIsNotFoundTest() {
        examService.addExam(1L, new AddExam(new Date()));

        verify(userRepository).findByUsername("username");
    }

    @Test(expected = CourseNotFoundException.class)
    public void shouldThrowExceptionWhenCourseIsNotFoundTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        examService.addExam(1L, new AddExam(new Date()));

        verify(userRepository).findByUsername("username");
        verify(courseRepository).findById(1L);
        verifyNoInteractions(examRepository);
    }

    @Test
    public void shouldSignForExamTest() {
        Exam exam = new Exam(new Date(), new Course(), new User());
        User user = new User();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(examRepository.findById(anyLong())).thenReturn(Optional.of(exam));

        examService.signInForExam(1L);
        verify(userRepository).findByUsername("username");
        verify(examRepository).findById(1L);
        verify(examRepository).save(any());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundWhileTryingToSignInForExamTest() {
        examService.signInForExam(1L);

        verify(userRepository).findByUsername("username");
        verifyNoInteractions(examRepository);
    }

    @Test(expected = ExamNotFoundException.class)
    public void shouldFindExamNotFoundWhenExamDoesNotExistTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        examService.signInForExam(1L);

        verify(examRepository).findById(1L);
    }

    @Test
    public void shouldFindAllInstructorExamsTest() throws ParseException {
        List exams = initInstructorExams();

        when(examRepository.findAllByInstructorUsernameOrderByExamDate(anyString())).thenReturn(exams);

        List<ExamDto> result = examService.findAllInstructorExams();

        assertTrue(result.size() == 2);
        verify(examRepository).findAllByInstructorUsernameOrderByExamDate("username");
    }

    private List<Exam> initInstructorExams() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Exam first = new Exam(sdf.parse("2020-10-25"), initCourse(), initInstructor());
        Exam second = new Exam(sdf.parse("2020-10-07"), initCourse(), initInstructor());

        List<Exam> exams = new ArrayList<>();
        exams.add(first);
        exams.add(second);

        return exams;
    }

    private User initUser() {
        return new User("username", "user@domain.com", "pass123", "Joe", "Doe");
    }

    private User initInstructor() {
        return new User("username", "instructor@domain.com", "instructor123", "Instructor", "Instructor");
    }

    private Course initCourse() {
        return new Course("Course name", "Course description", 1000, new Date(), 22, initInstructor());
    }
}
