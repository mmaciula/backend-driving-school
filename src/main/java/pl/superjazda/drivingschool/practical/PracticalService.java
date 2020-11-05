package pl.superjazda.drivingschool.practical;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.superjazda.drivingschool.course.Course;
import pl.superjazda.drivingschool.course.CourseRepository;
import pl.superjazda.drivingschool.exception.CourseNotFoundException;
import pl.superjazda.drivingschool.exception.PracticalNotFoundException;
import pl.superjazda.drivingschool.exception.UserNotFoundException;
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PracticalService {
    private PracticalRepository practicalRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private static final String PRACTICAL = "Practical not found";

    @Autowired
    public PracticalService(PracticalRepository practicalRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.practicalRepository = practicalRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public PracticalDto createNewPractical(AddPractical addPractical, Long courseId) {
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

        return new PracticalDto(practical);
    }

    public PracticalDto signUserForPractical(Long practicalId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        Optional<Practical> practical = practicalRepository.findById(practicalId);
        if (!practical.isPresent()) {
            throw new PracticalNotFoundException(PRACTICAL);
        }

        practical.get().setStudent(user.get());
        practicalRepository.save(practical.get());

        return new PracticalDto(practical.get());
    }

    public List<PracticalDto> findAllBookedPracticals() {
        String instructorUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Practical> activities = practicalRepository.findAllByInstructorUsernameAndStudentNotNull(instructorUsername);
        List<PracticalDto> dtos = new ArrayList<>();

        activities.forEach(practical -> {
            dtos.add(new PracticalDto(practical));
        });

        return dtos;
    }

    public List<PracticalDto> findAllCoursePracticals(Long courseId) {
        List<Practical> activities = practicalRepository.findAllByCourseId(courseId);
        List<PracticalDto> dtos = new ArrayList<>();

        activities.forEach(practical -> {
            dtos.add(new PracticalDto(practical));
        });

        return dtos;
    }

    public List<PracticalDto> findAllLoggedInUserPracticals() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Practical> activities = practicalRepository.findAllByStudentUsername(username);
        List<PracticalDto> dtos = new ArrayList<>();

        activities.forEach(practical -> {
            dtos.add(new PracticalDto(practical));
        });

        return dtos;
    }

    public PracticalDto ratePractical(Long id, int rate) {
        Optional<Practical> practical = practicalRepository.findById(id);
        if (!practical.isPresent()) {
            throw new PracticalNotFoundException(PRACTICAL);
        }
        practical.get().setInstructorRate(rate);
        practicalRepository.save(practical.get());

        return new PracticalDto(practical.get());
    }

    public PracticalDto commentPractical(Long id, String comment) {
        Optional<Practical> practical = practicalRepository.findById(id);
        if (!practical.isPresent()) {
            throw new PracticalNotFoundException(PRACTICAL);
        }
        practical.get().setComment(comment);
        practicalRepository.save(practical.get());

        return new PracticalDto(practical.get());
    }
}
