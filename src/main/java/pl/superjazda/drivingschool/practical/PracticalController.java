package pl.superjazda.drivingschool.practical;

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
import pl.superjazda.drivingschool.course.Course;
import pl.superjazda.drivingschool.course.CourseRepository;
import pl.superjazda.drivingschool.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.exception.PracticalNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.helpers.ResponseMessage;
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PracticalController {
    private PracticalRepository practicalRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;

    @Autowired
    public PracticalController(PracticalRepository practicalRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.practicalRepository = practicalRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/add/{courseId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> createPractical(@RequestBody AddPractical addPractical, @PathVariable Long courseId) {
        String instructorUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> instructor = userRepository.findByUsername(instructorUsername);
        if (!instructor.isPresent()) {
            throw new UserNotFoundException("Instructor not found");
        }
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }

        Practical practical = new Practical(addPractical.getDate(), course.get(), instructor.get());
        practicalRepository.save(practical);

        return ResponseEntity.ok(new ResponseMessage("Practical created successfully!"));
    }

    @PostMapping("/course/{practicalId}/signup")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<?> signUserForPractical(@PathVariable Long practicalId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        Optional<Practical> practical = practicalRepository.findById(practicalId);
        if (!practical.isPresent()) {
            throw new PracticalNotFoundException("Practical not found");
        }

        practical.get().setStudent(user.get());
        practicalRepository.save(practical.get());

        return ResponseEntity.ok(new PracticalDto(practical.get()));
    }

    @GetMapping("/occupied")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> findAllCoursePracticalWithSignInUser() {
        String instructorUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Practical> activities = practicalRepository.findAllByInstructorUsernameAndStudentNotNull(instructorUsername);
        List<PracticalDto> dtos = new ArrayList<>();

        activities.forEach(practical -> {
            dtos.add(new PracticalDto(practical));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> findAllPracticalForCourse(@PathVariable Long courseId) {
        List<Practical> activities = practicalRepository.findAllByCourseId(courseId);
        List<PracticalDto> dtos = new ArrayList<>();

        activities.forEach(practical -> {
            dtos.add(new PracticalDto(practical));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> findAllStudentsPracticals() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Practical> activities = practicalRepository.findAllByStudentUsername(username);
        List<PracticalDto> dtos = new ArrayList<>();

        activities.forEach(practical -> {
            dtos.add(new PracticalDto(practical));
        });

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/rate/{rate}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addRate(@PathVariable Long id, @PathVariable int rate) {
        Optional<Practical> practical = practicalRepository.findById(id);
        if (!practical.isPresent()) {
            throw new PracticalNotFoundException("Practical not found");
        }
        practical.get().setInstructorRate(rate);
        practicalRepository.save(practical.get());

        return ResponseEntity.ok(new PracticalDto(practical.get()));
    }

    @PostMapping("/{id}/comment")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody String comment) {
        Optional<Practical> practical = practicalRepository.findById(id);
        if (!practical.isPresent()) {
            throw new PracticalNotFoundException("Practical not found");
        }
        practical.get().setComment(comment);
        practicalRepository.save(practical.get());

        return ResponseEntity.ok(new PracticalDto(practical.get()));
    }
}
