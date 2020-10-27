package pl.superjazda.drivingschool.contact

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import pl.superjazda.drivingschool.contactform.ContactDto
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest extends Specification {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    def "should send message using contact form"() {
        given:
        String email;
        ContactDto contactDto = new ContactDto("Name", email, "This is contact form test")

        when:
            def response = mockMvc.perform(post("/api/contact")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(contactDto)))

        then:
            response.andExpect(status().isOk())
    }
}
