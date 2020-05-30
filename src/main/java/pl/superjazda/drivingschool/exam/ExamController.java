package pl.superjazda.drivingschool.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.exception.ExamNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExamController {
    private ExamRepository examRepository;
    private UserRepository userRepository;

    @Autowired
    public ExamController(ExamRepository examRepository, UserRepository userRepository) {
        this.examRepository = examRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAll() {
        List<Exam> exams = examRepository.findAll();
        List<ExamDto> dtos = new ArrayList<>();

        exams.forEach(exam -> {
            dtos.add(new ExamDto(exam));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> findAllExamsByCourseId(@PathVariable Long courseId) {
        List<Exam> exams = examRepository.findAllByCourseId(courseId);
        List<ExamDto> dtos = new ArrayList<>();

        exams.forEach(exam -> {
            dtos.add(new ExamDto(exam));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/student/{username}")
    public ResponseEntity<?> findAllByStudentUsername(@PathVariable String username) {
        List<Exam> exams = examRepository.findAllByStudentUsername(username);
        List<ExamDto> dtos = new ArrayList<>();

        exams.forEach(exam -> {
            dtos.add(new ExamDto(exam));
        });

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/instructor/{username}")
    public ResponseEntity<?> findAllByInstructorUsername(@PathVariable String username) {
        List<Exam> exams = examRepository.findAllByInstructorUsername(username);
        List<ExamDto> dtos = new ArrayList<>();

        exams.forEach(exam -> {
            dtos.add(new ExamDto(exam));
        });

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/signin/{id}")
    public ResponseEntity<?> signInForExam(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        Optional<Exam> exam = examRepository.findById(id);
        if (!exam.isPresent()) {
            throw new ExamNotFoundException("Exam not found");
        }

        exam.get().setStudent(user.get());
        exam.get().setOccupied(true);
        examRepository.save(exam.get());

        return ResponseEntity.ok(new ExamDto(exam.get()));
    }
}