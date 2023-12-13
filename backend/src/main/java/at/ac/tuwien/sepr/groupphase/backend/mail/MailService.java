package at.ac.tuwien.sepr.groupphase.backend.mail;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MailService {
    private final SecurityProperties securityProperties;
    private final JavaMailSender emailSender;
    private final JwtTokenizer jwtTokenizer;


    public void sendVerificationMail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("animalhub.service@gmail.com");
        message.setTo(email);
        message.setSubject("Animalhub e-mail confirmation");
        message.setText("http://localhost:4200/#/register/confirm/" + jwtTokenizer.getActivationToken(email));
        emailSender.send(message);
    }
}
