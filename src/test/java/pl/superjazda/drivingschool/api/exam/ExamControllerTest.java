package pl.superjazda.drivingschool.api.exam;

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
import pl.superjazda.drivingschool.api.user.UserRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldFindAllExamsTest() throws Exception {
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
        Long courseId = 1000002L;

        mockMvc.perform(get("/api/exam/course/{courseId}", courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(courseId));
    }

    @Test
    @WithMockUser(username = "studWithExams")
    public void shouldFindAllStudentExamsTest() throws Exception {
        mockMvc.perform(get("/api/exam/student"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentUsername").value("studWithExams"));
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldSignUpForAnExam() throws Exception {
        mockMvc.perform(put("/api/exam/signin/{id}", 3000002L))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Exam> examFromDB = examRepository.findById(3000002L);
        assertTrue(examFromDB.get().getOccupied());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldFindAllInstructorExamsTest() throws Exception {
        mockMvc.perform(get("/api/exam/instructor"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldAddNewExamTest() throws Exception {
        AddExam examToAdd = new AddExam(new Date());
        Long courseId = 1000002L;

        mockMvc.perform(post("/api/exam/add/{courseId}", courseId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(examToAdd)))
                .andDo(print())
                .andExpect(status().isOk());

        assertTrue(examRepository.findAllByCourseId(courseId).size() > 0);
    }
}
