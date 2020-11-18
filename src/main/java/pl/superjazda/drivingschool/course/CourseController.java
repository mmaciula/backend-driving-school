package pl.superjazda.drivingschool.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.helpers.ResponseMessage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {
    private CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage> addCourse(@Valid @RequestBody AddCourse addCourse) {
        courseService.addNewCourse(addCourse);

        return ResponseEntity.ok(new ResponseMessage("Course created successfully"));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getAllAvailableCoursesList() {
        List<CourseDto> courses = courseService.getListOfAllAvailableCourses();

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDto> findCourseById(@PathVariable Long courseId) {
        CourseDto course = courseService.findCourseById(courseId);

        return ResponseEntity.ok(course);
    }

    @GetMapping("/instructor")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<List<CourseDto>> findAllInstructorCourses() {
        List<CourseDto> courses = courseService.findAllInstructorCourses();

        return ResponseEntity.ok(courses);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);

        return ResponseEntity.ok(new ResponseMessage("Course deleted successfully"));
    }
}
