package pl.superjazda.drivingschool.api.practical;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.superjazda.drivingschool.api.course.CourseRepository;
import pl.superjazda.drivingschool.api.user.User;
import pl.superjazda.drivingschool.api.user.UserRepository;

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

    @Test
    @WithMockUser(roles = "MODERATOR", username = "instructor")
    public void shouldAddNewCoursePracticalTest() throws Exception {
        Long courseId = 1000002L;
        AddPractical addPractical = new AddPractical(new Date());

        mockMvc.perform(post("/api/activity/add/{courseId}", courseId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(addPractical)))
                .andDo(print())
                .andExpect(status().isOk());

        List<Practical> practicals = practicalRepository.findAllByCourseId(courseId);

        assertTrue(practicals.size() == 3);
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldSignUserForPracticalTest() throws Exception {
        Long practicalInDBId = 2000002L;

        mockMvc.perform(post("/api/activity/course/{practicalId}/signup", practicalInDBId))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Practical> practicalBooked = practicalRepository.findById(practicalInDBId);

        assertTrue(practicalBooked.get().getStudent() != null);
        assertTrue(practicalBooked.get().getStudent().getUsername().equals("student"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR", username = "instructor")
    public void shouldFindAllCoursePracticalsWithSignUserTest() throws Exception {
        Optional<Practical> practical = practicalRepository.findById(2000002L);
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
        Long courseId = 1000002L;

        mockMvc.perform(get("/api/activity/course/{courseId}", courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].course.id").value(courseId));
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldFindAllStudentPracticals() throws Exception {
        Long practicalId = 2000002L;
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
        Long practicalId = 2000002L;
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
        Long practicalId = 2000002L;
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
