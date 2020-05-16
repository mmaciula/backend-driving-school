package pl.superjazda.drivingschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.model.Course;
import pl.superjazda.drivingschool.model.ResponseMessage;
import pl.superjazda.drivingschool.model.User;
import pl.superjazda.drivingschool.model.dto.CourseDto;
import pl.superjazda.drivingschool.repository.CourseRepository;
import pl.superjazda.drivingschool.repository.UserRepository;

import javax.validation.Valid;

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
    public ResponseEntity<?> addCourse(@Valid @RequestBody CourseDto courseDto) {
        if (courseRepository.existsByName(courseDto.getName())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Course already exists!"));
        }

        User instructor = userRepository.findByUsername(courseDto.getInstructorUsername())
                .orElseThrow(() -> new UserNotFoundException("Instructor not found: " + courseDto.getInstructorUsername()));

        Course newCourse = new Course(courseDto, instructor);
        courseRepository.save(newCourse);

        return ResponseEntity.ok(new ResponseMessage("Course created successfully"));
    }
}
