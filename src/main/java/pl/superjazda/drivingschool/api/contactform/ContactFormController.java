package pl.superjazda.drivingschool.api.contactform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.superjazda.drivingschool.helpers.ResponseMessage;

@RestController
@RequestMapping("/api/contact")
//@CrossOrigin(origins = "*", maxAge = 3600)
public class ContactFormController {
    private EmailService emailService;

    @Autowired
    public ContactFormController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> sendMessage(@RequestBody ContactDto message) {
        emailService.contactForm(message);
        return ResponseEntity.ok(new ResponseMessage("Message sent"));
    }
}
