package pl.superjazda.drivingschool.course;

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
import pl.superjazda.drivingschool.user.User;
import pl.superjazda.drivingschool.user.UserRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Before
    public void setUp() {
        User instructor = new User("instructor", "instructor@domain.com", "pass123", "Joe", "Doe");
        userRepository.save(instructor);
        Course course = new Course("Course", "Some course description", 1200, new Date(), 12, instructor);
        courseRepository.save(course);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldCreateNewCourseTest() throws Exception {
        AddCourse addCourse = new AddCourse("Name", "Description", 1000, new Date(), "instructor", 12);

        mockMvc.perform(post("/api/course/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(addCourse)))
                .andDo(print())
                .andExpect(status().isOk());

        Course course = courseRepository.findByName(addCourse.getName());
        assertTrue(course.getDescription().equals(addCourse.getDescription()));
    }

    @Test
    @WithMockUser(username = "joe")
    public void shouldGetListOfAllAvailableCoursesForCurrentlyLoggedUserTest() throws Exception {
        // TODO Look at TODO inside ExamControllerTest class
        mockMvc.perform(get("/api/course/courses"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void shouldFindCourseByIdTest() throws Exception {
        Long courseId = 1000001L;

        mockMvc.perform(get("/api/course/courses/{courseId}", courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseId));
    }

    @Test
    @WithMockUser
    public void shouldNotFindCourseByIdTest() throws Exception {
        mockMvc.perform(get("/api/course/courses/{courseId}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldDeleteChosenCourseTest() throws Exception {
        mockMvc.perform(delete("/api/course/delete/{id}", 1000001L))
                .andExpect(status().isOk());

        Optional<Course> course = courseRepository.findById(1000001L);
        assertFalse(course.isPresent());
    }
}
