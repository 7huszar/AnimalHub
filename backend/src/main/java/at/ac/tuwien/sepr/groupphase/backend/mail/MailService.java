package at.ac.tuwien.sepr.groupphase.backend.mail;

import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Component offering services to send e-mail.
 * Emails are sent in own threads.
 */
@RequiredArgsConstructor
@Component
public class MailService {
    private final JavaMailSender emailSender;
    private final JwtTokenizer jwtTokenizer;

    /**
     * Send a verification link to specified e-mail.
     * Token is valid for 10 min. {@link at.ac.tuwien.sepr.groupphase.backend.config.SecurityPropertiesConfig}.
     * E-Mail is sent in own thread.
     *
     * @param email E-mail to be verified
     */
    @Async
    public void sendVerificationMail(String email, String language) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("animalhub.service@gmail.com");
        message.setTo(email);
        message.setSubject("Animalhub e-mail confirmation");
        message.setText("http://localhost:4200/#/register/confirm/" + jwtTokenizer.getActivationToken(email, language));
        emailSender.send(message);
    }

    @Async
    public void sendResetPasswordMail(String email, String language) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("animalhub.service@gmail.com");
        message.setTo(email);
        message.setSubject("Animal Hub reset password");
        message.setText("http://localhost:4200/#/tmp/" + jwtTokenizer.getPasswordToken(email, language));
        emailSender.send(message);
    }
}
