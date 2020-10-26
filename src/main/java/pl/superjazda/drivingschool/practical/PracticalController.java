package pl.superjazda.drivingschool.practical;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.helpers.ResponseMessage;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PracticalController {
    private PracticalService practicalService;

    @Autowired
    public PracticalController(PracticalService practicalService) {
        this.practicalService = practicalService;
    }

    @PostMapping("/add/{courseId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createPractical(@RequestBody AddPractical addPractical, @PathVariable Long courseId) {
        PracticalDto practicalDto = practicalService.createNewPractical(addPractical, courseId);

        return ResponseEntity.ok(new ResponseMessage("Practical created successfully!"));
    }

    @PostMapping("/course/{practicalId}/signup")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> signUserForPractical(@PathVariable Long practicalId) {
        PracticalDto practicalDto = practicalService.signUserForPractical(practicalId);

        return ResponseEntity.ok(practicalDto);
    }

    @GetMapping("/occupied")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> findAllCoursePracticalWithSignInUser() {
        List<PracticalDto> practicals = practicalService.findAllBookedPracticals();

        return ResponseEntity.ok(practicals);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> findAllPracticalForCourse(@PathVariable Long courseId) {
        List<PracticalDto> coursePracticals = practicalService.findAllCoursePracticals(courseId);

        return ResponseEntity.ok(coursePracticals);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findAllStudentsPracticals() {
        List<PracticalDto> studentPracticals = practicalService.findAllLoggedInUserPracticals();

        return ResponseEntity.ok(studentPracticals);
    }

    @PostMapping("/{id}/rate/{rate}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addRate(@PathVariable Long id, @PathVariable int rate) {
        PracticalDto practical = practicalService.ratePractical(id, rate);

        return ResponseEntity.ok(practical);
    }

    @PostMapping("/{id}/comment")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody String comment) {
        PracticalDto practical = practicalService.commentPractical(id, comment);

        return ResponseEntity.ok(practical);
    }
}
