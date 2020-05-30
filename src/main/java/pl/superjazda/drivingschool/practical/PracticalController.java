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
import pl.superjazda.drivingschool.exception.PracticalNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
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

    @Autowired
    public PracticalController(PracticalRepository practicalRepository, UserRepository userRepository) {
        this.practicalRepository = practicalRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/course/{practicalId}/signup")
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

    @GetMapping("/course/{courseId}/student")
    public ResponseEntity<?> findAllCoursePracticalWithSignInUser(@PathVariable Long courseId) {
        List<Practical> activities = practicalRepository.findAllByCourseIdAndStudentNotNull(courseId);
        List<PracticalDto> dtos = new ArrayList<>();

        activities.forEach(practical -> {
            dtos.add(new PracticalDto(practical));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> findAllPractical(@PathVariable Long courseId) {
        List<Practical> activities = practicalRepository.findAllByCourseId(courseId);
        List<PracticalDto> dtos = new ArrayList<>();

        activities.forEach(practical -> {
            dtos.add(new PracticalDto(practical));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/course/{courseId}/mine")
    public ResponseEntity<?> findAllStudentsPracticals(@PathVariable Long courseId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Practical> activities = practicalRepository.findAllByCourseIdAndStudentUsername(courseId, username);
        List<PracticalDto> dtos = new ArrayList<>();

        activities.forEach(practical -> {
            dtos.add(new PracticalDto(practical));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/course/{id}/dates")
    public ResponseEntity<?> findPracticalDatesByCourseId(@PathVariable Long id) {
        List<Practical> activities = practicalRepository.findAllByCourseIdOrderByDate(id);
        List<Date> activityDates = new ArrayList<>();

        activities.forEach(practical -> {
            activityDates.add(practical.getDate());
        });

        return ResponseEntity.ok(activityDates);
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
