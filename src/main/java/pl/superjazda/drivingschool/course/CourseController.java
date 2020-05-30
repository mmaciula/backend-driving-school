package pl.superjazda.drivingschool.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.helpers.ResponseMessage;
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {
    private CourseRepository courseRepository;
    private UserRepository userRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCourse(@Valid @RequestBody AddCourse addCourse) {
        if (courseRepository.existsByName(addCourse.getName())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Course already exists!"));
        }

        Optional<User> instructor = userRepository.findByUsername(addCourse.getInstructorUsername());
        if (!instructor.isPresent()) {
            throw new UserNotFoundException("Instructor not found");
        }

        Course newCourse = new Course(addCourse, instructor.get());
        courseRepository.save(newCourse);

        return ResponseEntity.ok(new ResponseMessage("Course created successfully"));
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getAllAvailableCoursesList() {
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> dtos = new ArrayList<>();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        courses.forEach(course -> {
            if (!course.getInstructor().getSurname().equals(username)) {
                dtos.add(new CourseDto(course));
            }
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<?> findCourseById(@PathVariable Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }
        CourseDto foundCourse = new CourseDto(course.get());

        return ResponseEntity.ok(foundCourse);
    }
}
