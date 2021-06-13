package pl.superjazda.drivingschool.contact

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.util.ReflectionTestUtils
import pl.superjazda.drivingschool.api.contactform.ContactDto
import pl.superjazda.drivingschool.api.contactform.EmailService
import spock.lang.Specification

class EmailServiceTest extends Specification {
    private JavaMailSender javaMailSender;
    private EmailService emailService;

    void setup() {
        javaMailSender = Mock(JavaMailSender.class)
        emailService = new EmailService(javaMailSender)
        ReflectionTestUtils.setField(emailService, "email", "mail@domain.com")
    }

    def "should create and send email to sender and to school"() {
        given:
        ContactDto contact = new ContactDto("Joe", "joe@doe.com", "Test message")

        when:
        emailService.contactForm(contact)

        then:
        2 * javaMailSender.send(_)
    }
}
