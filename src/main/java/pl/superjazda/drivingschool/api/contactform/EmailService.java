package pl.superjazda.drivingschool.api.contactform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void contactForm(ContactDto contact) {
        StringBuilder messageForSender = new StringBuilder().append("Dziękujemy za wiadomość. Odpowiemy tak szybko jak to możliwe\n\n")
                .append("Twoja wiadomość:\n").append(contact.getContent()).append("\nSzerokiej drogi");
        SimpleMailMessage toUser = new SimpleMailMessage();
        toUser.setTo(contact.getEmail());
        toUser.setFrom(email);
        toUser.setSubject("Super jazda");
        toUser.setText(messageForSender.toString());
        javaMailSender.send(toUser);

        StringBuilder messageForSchool = new StringBuilder().append("Od: ").append(contact.getEmail()).append("\n\n").append(contact.getContent());
        SimpleMailMessage toSchool = new SimpleMailMessage();
        toSchool.setTo(email);
        toSchool.setFrom(email);
        toSchool.setSubject("Wiadomość od: " + contact.getName());
        toSchool.setText(messageForSchool.toString());
        javaMailSender.send(toSchool);
    }
}
