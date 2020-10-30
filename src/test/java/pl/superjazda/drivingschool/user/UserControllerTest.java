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
    private static boolean initTest = false;

    @Before
    public void setUp() {
        if (!initTest) {
            User user = new User("student", "stud@domain.com", "stud123", "Joe", "Doe");
            User admin = new User("administrator", "admin@domain.com", "admin123", "John", "Smith");
            userRepository.save(user);
            userRepository.save(admin);

            initTest = true;
        }
    }

    @Test
    @WithMockUser(username = "administrator", roles = "ADMIN")
    public void shouldFindAllRegisteredUsersTest() throws Exception {
        Optional<User> user = userRepository.findByUsername("student");

        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "student")
    public void shouldAssignUserToCourseTest() throws Exception {
        User instructor = new User("instructor", "instructor@domain.com", "instructor123", "Steven", "Instructor");
        userRepository.save(instructor);
        Course course = new Course("Course name", "Course description", 500, new Date(), 10, instructor);
        courseRepository.save(course);

        Course courseFromDB = courseRepository.findByName(course.getName());
        Long courseId = courseFromDB.getId();

        mockMvc.perform(put("/api/users/course/add/{id}", courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses[0].id").value(courseId));
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
