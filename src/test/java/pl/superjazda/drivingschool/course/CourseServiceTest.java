package pl.superjazda.drivingschool.course;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.superjazda.drivingschool.exception.CourseAlreadyExistsException;
import pl.superjazda.drivingschool.exception.CourseHavePracticalException;
import pl.superjazda.drivingschool.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.practical.Practical;
import pl.superjazda.drivingschool.practical.PracticalRepository;
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    public void createNewUserTest() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
        when(courseRepository.save(any(Course.class))).thenReturn(new Course());

        CourseDto courseDto = courseService.addNewCourse(addCourse);

        Assert.assertTrue(courseDto.getName().equals(addCourse.getName()));
    }

    @Test(expected = CourseAlreadyExistsException.class)
    public void shouldReturnCourseAlreadyExistsExceptionTest() {
        when(courseRepository.existsByName(anyString())).thenReturn(true);

        courseService.addNewCourse(addCourse);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldReturnInstructorNotFoundExceptionTest() {
        when(courseRepository.existsByName(anyString())).thenReturn(false);

        courseService.addNewCourse(addCourse);
    }

    @Test(expected = CourseNotFoundException.class)
    public void shouldThrownExceptionWhileTryingToFindCourseThatNotExist() {
        courseService.findCourseById(1L);
    }

    @Test(expected = CourseHavePracticalException.class)
    public void shouldThrownExceptionWhenCourseHavePracticalsTest() {
        List<Practical> coursePracticals = mock(ArrayList.class);

        when(courseRepository.findById(any())).thenReturn(Optional.of(new Course()));
        when(practicalRepository.findAllByCourseId(any())).thenReturn(coursePracticals);

        courseService.delete(1L);
    }

    @Test
    public void shouldDeleteCourseTest() {
        Long courseId = 57L;

        when(courseRepository.findById(any())).thenReturn(Optional.of(new Course()));

        courseService.delete(courseId);

        verify(courseRepository, times(1)).deleteById(eq(courseId));
    }
}
