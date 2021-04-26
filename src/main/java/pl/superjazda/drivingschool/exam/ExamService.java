package pl.superjazda.drivingschool.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.superjazda.drivingschool.course.Course;
import pl.superjazda.drivingschool.course.CourseRepository;
import pl.superjazda.drivingschool.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.exception.ExamNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserDto;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {
    private ExamRepository examRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;

    @Autowired
    public ExamService(ExamRepository examRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public ExamDto addExam(Long id, AddExam addExam) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found");
        }

        Exam newExam = createNewExam(addExam, user.get(), course.get());
        examRepository.save(newExam);

        return new ExamDto(newExam);
    }

    public List<ExamDto> findAllExams() {
        List<Exam> exams = examRepository.findAll();
        List<ExamDto> dtos = new ArrayList<>();

        exams.forEach(exam -> dtos.add(new ExamDto(exam)));

        return dtos;
    }

    public List<ExamDto> findAllCourseExamsById(Long id) {
        List<Exam> exams = examRepository.findAllByCourseId(id);
        List<ExamDto> dtos = new ArrayList<>();

        exams.forEach(exam -> dtos.add(new ExamDto(exam)));

        return dtos;
    }

    public List<ExamDto> findAllStudentExams() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Exam> exams = examRepository.findAllByStudentUsername(username);
        List<ExamDto> dtos = new ArrayList<>();

        exams.forEach(exam -> dtos.add(new ExamDto(exam)));

        return dtos;
    }

    public List<ExamDto> findAllInstructorExams() {
        String instructorUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Exam> exams = examRepository.findAllByInstructorUsernameOrderByExamDate(instructorUsername);
        List<ExamDto> dtos = new ArrayList();

        exams.forEach(exam -> dtos.add(new ExamDto(exam)));

        return dtos;
    }

    public ExamDto signInForExam(Long id) {
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

        return new ExamDto(exam.get());
    }

    private Exam createNewExam(AddExam addExam, User user, Course course) {
        UserDto userDto = new UserDto(user);

        if (userDto.getRoles().contains("MODERATOR")) {
            return new Exam(addExam.getDate(), course, user);
        } else if (userDto.getRoles().contains("ADMIN")) {
            return new Exam(addExam.getDate(), course, course.getInstructor());
        } else {
            return new Exam(addExam.getDate(), user, course);
        }
    }
}
