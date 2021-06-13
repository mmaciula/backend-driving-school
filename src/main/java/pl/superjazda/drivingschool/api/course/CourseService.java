package pl.superjazda.drivingschool.api.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.superjazda.drivingschool.web.exception.CourseAlreadyExistsException;
import pl.superjazda.drivingschool.web.exception.CourseHavePracticalException;
import pl.superjazda.drivingschool.web.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.web.exception.UserNotFoundException;
import pl.superjazda.drivingschool.api.practical.Practical;
import pl.superjazda.drivingschool.api.practical.PracticalRepository;
import pl.superjazda.drivingschool.api.user.User;
import pl.superjazda.drivingschool.api.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private PracticalRepository practicalRepository;

    @Autowired
    public CourseService (CourseRepository courseRepository, UserRepository userRepository, PracticalRepository practicalRepository) {
        this.courseRepository  = courseRepository;
        this.userRepository = userRepository;
        this.practicalRepository = practicalRepository;
    }

    public CourseDto addNewCourse(AddCourse addCourse) {
        if (courseRepository.existsByName(addCourse.getName())) {
            throw new CourseAlreadyExistsException("Course already exists");
        }
        Optional<User> instructor = userRepository.findByUsername(addCourse.getInstructorUsername());
        if (!instructor.isPresent()) {
            throw new UserNotFoundException("Instructor not found");
        }
        Course newCourse = new Course(addCourse, instructor.get());
        courseRepository.save(newCourse);

        return new CourseDto(newCourse);
    }

    public List<CourseDto> getListOfAllAvailableCourses() {
        List<Course> allCourses = courseRepository.findAll();
        List<CourseDto> dtos = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        allCourses.forEach(course -> {
            if (!course.getInstructor().getUsername().equals(username)) {
                dtos.add(new CourseDto(course));
            }
        });

        return dtos;
    }

    public CourseDto findCourseById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }

        return new CourseDto(course.get());
    }

    public List<CourseDto> findAllInstructorCourses() {
        String instructorUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Course> instructorCourses = courseRepository.findAllByInstructorUsername(instructorUsername);
        if (instructorCourses.isEmpty()) {
            return new ArrayList<>();
        }

        List<CourseDto> dtos = new ArrayList<>();
        instructorCourses.forEach(course -> dtos.add(new CourseDto(course)));

        return dtos;
    }

    public CourseDto delete(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }

        List<Practical> coursePracticals = practicalRepository.findAllByCourseId(id);
        if (!coursePracticals.isEmpty()) {
            throw new CourseHavePracticalException("Can't remove this course");
        }

        CourseDto removedCourse = new CourseDto(course.get());
        courseRepository.deleteById(id);

        return removedCourse;
    }
}
