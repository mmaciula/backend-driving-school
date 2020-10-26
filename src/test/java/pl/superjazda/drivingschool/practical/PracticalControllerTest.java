package pl.superjazda.drivingschool.practical;

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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PracticalControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PracticalRepository practicalRepository;

    @Before
    public void setUp() {
        User instructor = new User("instructor", "instructor@domain.com", "password", "Joe", "Doe");
        User student = new User("student", "student@domain.com", "test123", "John", "Smith");
        userRepository.save(instructor);
        userRepository.save(student);
        Course course = new Course("Name", "Description", 1200, new Date(), 8, instructor);
        courseRepository.save(course);
        Practical practical = new Practical(new Date(), course, instructor);
        practicalRepository.save(practical);
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "instructor")
    public void shouldAddNewCourseTest() throws Exception {
        AddPractical addPractical = new AddPractical(new Date());

        mockMvc.perform(post("/api/activity/add/{courseId}", 1000001L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(addPractical)))
                .andDo(print())
                .andExpect(status().isOk());

        List<Practical> practicals = practicalRepository.findAllByCourseId(1000001L);

        assertTrue(practicals.size() == 2);
        assertTrue(practicals.get(0).getCourse().getId() == 1000001L);
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldSignUserForPracticalTest() throws Exception {
        mockMvc.perform(post("/api/activity/course/{practicalId}/signup", 2000001L))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Practical> practicalBooked = practicalRepository.findById(2000001L);

        assertTrue(practicalBooked.get().getStudent() != null);
        assertTrue(practicalBooked.get().getStudent().getUsername().equals("student"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR", username = "instructor")
    public void shouldFindAllCoursePracticalsWithSignUserTest() throws Exception {
        Optional<Practical> practical = practicalRepository.findById(2000001L);
        Optional<User> student = userRepository.findByUsername("student");
        practical.get().setStudent(student.get());
        practicalRepository.save(practical.get());

        mockMvc.perform(get("/api/activity/occupied"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student.username").value(student.get().getUsername()));
    }

    @Test
    @WithMockUser
    public void shouldFindAllCoursePracticalsTest() throws Exception {
        Long courseId = 1000001L;

        mockMvc.perform(get("/api/activity/course/{courseId}", courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].course.id").value(courseId));
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldFindAllStudentPracticals() throws Exception {
        Long practicalId = 2000001L;
        Optional<User> student = userRepository.findByUsername("student");
        Optional<Practical> practical = practicalRepository.findById(practicalId);
        practical.get().setStudent(student.get());
        practicalRepository.save(practical.get());

        mockMvc.perform(get("/api/activity/mine"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(practicalId));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldRatePracticalTest() throws Exception {
        Long practicalId = 2000001L;
        int rate = 4;

        mockMvc.perform(post("/api/activity/{id}/rate/{rate}", practicalId, rate))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Practical> practical = practicalRepository.findById(practicalId);

        assertTrue(practical.get().getInstructorRate() == rate);
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldAddCommentToPracticalTest() throws Exception {
        Long practicalId = 2000001L;
        String comment = "Instructor comment about the practical";

        mockMvc.perform(post("/api/activity/{id}/comment", practicalId)
                .contentType("application/json")
                .content(comment))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Practical> practical = practicalRepository.findById(practicalId);

        assertTrue(practical.get().getComment().equals(comment));
    }
}
