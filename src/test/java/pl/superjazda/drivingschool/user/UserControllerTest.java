package pl.superjazda.drivingschool.user;

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
import pl.superjazda.drivingschool.role.Role;
import pl.superjazda.drivingschool.role.RoleRepository;
import pl.superjazda.drivingschool.role.RoleType;

import java.util.Date;
import java.util.Optional;

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
    @WithMockUser(username = "administrator", roles = "ADMIN")
    public void shouldFindAllRegisteredUsersTest() throws Exception {
        // TODO Check return array
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
        Role adminRole = new Role(RoleType.ROLE_ADMIN);
        roleRepository.save(adminRole);

        mockMvc.perform(post("/api/users/roles/assign/{role}/{username}", role, username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[0]").value(role));
    }
}
