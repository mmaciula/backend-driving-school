package pl.superjazda.drivingschool.api.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.superjazda.drivingschool.api.course.Course;
import pl.superjazda.drivingschool.api.course.CourseRepository;
import pl.superjazda.drivingschool.api.role.RoleRepository;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @WithMockUser(username = "student")
    public void shouldShowLoggedUserTest() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andDo(print())
                .andExpect(jsonPath("$.username").value("student"));
    }

    @Test
    @WithMockUser(username = "administrator", roles = "ADMIN")
    public void shouldFindAllRegisteredUsersTest() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldAssignUserToCourseTest() throws Exception {
        Optional<Course> course = courseRepository.findById(1000002L);

        mockMvc.perform(put("/api/users/course/add/{id}", course.get().getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses[0].id").value(course.get().getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAssignNewRoleToUser() throws Exception {
        String username = "student";
        String role = "ROLE_ADMIN";

        mockMvc.perform(post("/api/users/roles/assign/{role}/{username}", role, username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[0]").value(role));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldDeleteUserFromDatabaseTest() throws Exception {
        mockMvc.perform(delete("/api/users/delete/{username}", "delete"))
                .andExpect(status().isOk());

        Optional<User> user = userRepository.findByUsername("delete");

        assertTrue(user.isEmpty());
    }
}
