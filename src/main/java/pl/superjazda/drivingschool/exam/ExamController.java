package pl.superjazda.drivingschool.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.helpers.ResponseMessage;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
//@CrossOrigin(origins = "*", maxAge = 3600)
public class ExamController {
    private ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/add/{courseId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage> addExam(@PathVariable Long courseId, @RequestBody AddExam addExam) {
        examService.addExam(courseId, addExam);

        return ResponseEntity.ok(new ResponseMessage("Exam created successfully!"));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExamDto>> findAll() {
        List<ExamDto> exams = examService.findAllExams();

        return ResponseEntity.ok(exams);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<List<ExamDto>> findAllExamsByCourseId(@PathVariable Long courseId) {
        List<ExamDto> exams = examService.findAllCourseExamsById(courseId);

        return ResponseEntity.ok(exams);
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ExamDto>> findAllByStudentUsername() {
        List<ExamDto> studentExams = examService.findAllStudentExams();

        return ResponseEntity.ok(studentExams);
    }

    @GetMapping("/instructor")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<List<ExamDto>> findAllByInstructorUsername() {
        List<ExamDto> instructorExams = examService.findAllInstructorExams();

        return ResponseEntity.ok(instructorExams);
    }

    @PutMapping("/signin/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ExamDto> signInForExam(@PathVariable Long id) {
        ExamDto exam = examService.signInForExam(id);

        return ResponseEntity.ok(exam);
    }
}
