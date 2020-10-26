package pl.superjazda.drivingschool.exam;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.superjazda.drivingschool.course.Course;
import pl.superjazda.drivingschool.course.CourseRepository;
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExamControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExamRepository examRepository;

    @Before
    public void setUp() {
        User instructor = new User("instructor", "instructor@domain.com", "test123", "Joe", "Doe");
        userRepository.save(instructor);
        Course course = new Course("Course name", "Course description", 2500, new Date(), 18, instructor);
        courseRepository.save(course);
        Exam exam = new Exam(new Date(), course, instructor);
        examRepository.save(exam);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldFindAllExamsTest() throws Exception {
        // TODO Research for a ways to check returned list
        mockMvc.perform(get("/api/exam/list"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void shouldReturnAccessDeniedWhenUserIsNotAdminTest() throws Exception {
        mockMvc.perform(get("/api/exam/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldFindAllCourseExamsByIdTest() throws Exception{
        Long courseId = 1000001L;

        mockMvc.perform(get("/api/exam/course/{courseId}", courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(courseId));
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldFindAllStudentExamsTest() throws Exception {
        User user = new User("student", "stud@domain.com", "password", "John", "Mock");
        userRepository.save(user);
        Optional<Exam> exam = examRepository.findById(3000001L);
        exam.get().setStudent(user);
        examRepository.save(exam.get());

        mockMvc.perform(get("/api/exam/student"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentUsername").value("student"));
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldSignUpForAnExam() throws Exception {
        User user = new User("student", "stud@domain.com", "password", "John", "Smith");
        userRepository.save(user);

        mockMvc.perform(put("/api/exam/signin/{id}", 3000001))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Exam> examFromDB = examRepository.findById(3000001L);
        assertTrue(examFromDB.get().getOccupied());
    }
}
