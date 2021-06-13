package pl.superjazda.drivingschool.api.course;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.superjazda.drivingschool.web.exception.CourseAlreadyExistsException;
import pl.superjazda.drivingschool.web.exception.CourseHavePracticalException;
import pl.superjazda.drivingschool.web.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.web.exception.UserNotFoundException;
import pl.superjazda.drivingschool.api.practical.Practical;
import pl.superjazda.drivingschool.api.practical.PracticalRepository;
import pl.superjazda.drivingschool.api.user.User;
import pl.superjazda.drivingschool.api.user.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PracticalRepository practicalRepository;
    @InjectMocks
    private CourseService courseService;
    private AddCourse addCourse;

    @Before
    public void setUp() {
        addCourse = new AddCourse();
        addCourse.setName("test");
        addCourse.setInstructorUsername("InstructorUsername");
    }

    @Test
    public void createNewCourseTest() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
        when(courseRepository.save(any(Course.class))).thenReturn(new Course());

        CourseDto courseDto = courseService.addNewCourse(addCourse);

        assertTrue(courseDto.getName().equals(addCourse.getName()));
    }

    @Test(expected = CourseAlreadyExistsException.class)
    public void shouldReturnCourseAlreadyExistsExceptionTest() {
        when(courseRepository.existsByName(anyString())).thenReturn(true);

        courseService.addNewCourse(addCourse);
        verify(courseRepository).existsByName("test");
        verifyNoInteractions(userRepository);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldReturnInstructorNotFoundExceptionTest() {
        when(courseRepository.existsByName(anyString())).thenReturn(false);

        courseService.addNewCourse(addCourse);
        verify(courseRepository).existsByName("test");
        verify(userRepository).findByUsername("InstructorUsername");
        verifyNoMoreInteractions(courseRepository);
    }

    @Test(expected = CourseNotFoundException.class)
    public void shouldThrownExceptionWhileTryingToFindCourseThatNotExist() {
        courseService.findCourseById(1L);

        verify(courseRepository).findById(1L);
    }

    @Test
    public void shouldFindAllInstructorCoursesTest() {
        mockSecurityContext();

        when(courseRepository.findAllByInstructorUsername("username")).thenReturn(initListOfCourses());

        List<CourseDto> instructorCourses = courseService.findAllInstructorCourses();

        assertTrue(instructorCourses.size() == 2);
        verify(courseRepository).findAllByInstructorUsername("username");
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    public void shouldReturnEmptyArrayWhenInstructorDoesNotHaveCourses() {
        mockSecurityContext();
        when(courseRepository.findAllByInstructorUsername(anyString())).thenReturn(new ArrayList<>());

        List<CourseDto> instructorCourses = courseService.findAllInstructorCourses();

        assertTrue(instructorCourses.isEmpty());
        verify(courseRepository).findAllByInstructorUsername("username");
    }

    @Test(expected = CourseNotFoundException.class)
    public void shouldThrowCourseNotFindWhileDeletingTest() {
        courseService.delete(1L);

        verify(courseRepository).findById(1L);
        verifyNoInteractions(practicalRepository);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test(expected = CourseHavePracticalException.class)
    public void shouldThrownCourseHavePracticalsWhenTryingToDeleteTest() {
        List<Practical> coursePracticals = mock(ArrayList.class);

        when(courseRepository.findById(any())).thenReturn(Optional.of(new Course()));
        when(practicalRepository.findAllByCourseId(any())).thenReturn(coursePracticals);

        courseService.delete(1L);

        verify(courseRepository).findById(1L);
        verify(practicalRepository).findAllByCourseId(1L);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    public void shouldDeleteCourseTest() {
        Long courseId = 57L;

        when(courseRepository.findById(any())).thenReturn(Optional.of(new Course()));

        courseService.delete(courseId);

        verify(courseRepository, times(1)).deleteById(eq(courseId));
    }

    private User initInstructor() {
        return new User("username", "instructor@domain.com", "pass123", "Joe", "Doe");
    }

    private List<Course> initListOfCourses() {
        Course first = new Course("First", "First course description", 1000, new Date(), 11, initInstructor());
        Course second = new Course("Second", "Second Course description", 2000, new Date(), 22, initInstructor());

        List<Course> courses = new ArrayList<>();
        courses.add(first);
        courses.add(second);

        return courses;
    }

    private void mockSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
    }
}
