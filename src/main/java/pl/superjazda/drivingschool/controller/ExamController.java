package pl.superjazda.drivingschool.controller;

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
import pl.superjazda.drivingschool.model.Exam;
import pl.superjazda.drivingschool.model.User;
import pl.superjazda.drivingschool.model.dto.ExamDto;
import pl.superjazda.drivingschool.repository.ExamRepository;
import pl.superjazda.drivingschool.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/course/{id}")
    public ResponseEntity<?> findAllExamsByCourseId(@PathVariable Long id) {
        List<Exam> exams = examRepository.findAllByCourseId(id);
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
        List<Exam> exams = examRepository.findAllByStudentUsername(username);
        List<ExamDto> dtos = new ArrayList<>();

        exams.forEach(exam -> {
            dtos.add(new ExamDto(exam));
        });

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/signin/{id}")
    public ResponseEntity<?> signInForExam(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        Exam exam = examRepository.findById(id).orElseThrow(() -> new ExamNotFoundException("Exam not found"));

        exam.setStudent(user);
        exam.setOccupied(true);
        examRepository.save(exam);

        return ResponseEntity.ok(new ExamDto(exam));
    }
}
