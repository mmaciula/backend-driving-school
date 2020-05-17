package pl.superjazda.drivingschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import pl.superjazda.drivingschool.model.Course;
import pl.superjazda.drivingschool.model.ResponseMessage;
import pl.superjazda.drivingschool.model.User;
import pl.superjazda.drivingschool.model.dto.CourseDto;
import pl.superjazda.drivingschool.model.dto.NewCourseDto;
import pl.superjazda.drivingschool.repository.CourseRepository;
import pl.superjazda.drivingschool.repository.UserRepository;

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
    public ResponseEntity<?> addCourse(@Valid @RequestBody NewCourseDto newCourseDto) {
        if (courseRepository.existsByName(newCourseDto.getName())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Course already exists!"));
        }

        User instructor = userRepository.findByUsername(newCourseDto.getInstructorUsername())
                .orElseThrow(() -> new UserNotFoundException("Instructor not found: " + newCourseDto.getInstructorUsername()));

        Course newCourse = new Course(newCourseDto, instructor);
        courseRepository.save(newCourse);

        return ResponseEntity.ok(new ResponseMessage("Course created successfully"));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getAllAvailableCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> dtos = new ArrayList<>();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        courses.forEach(course -> {
            if (!course.getInstructor().getSurname().equals(username)) {
                dtos.add(new CourseDto(course));
            }
        });

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDto> findCourseById(@PathVariable Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }
        CourseDto foundCourse = new CourseDto(course.get());

        return new ResponseEntity<>(foundCourse, HttpStatus.OK);
    }
}
