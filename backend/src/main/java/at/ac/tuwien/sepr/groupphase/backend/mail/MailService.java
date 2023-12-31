package at.ac.tuwien.sepr.groupphase.backend.mail;

import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Component offering services to send e-mail.
 * Emails are sent in own threads.
 */
@RequiredArgsConstructor
@Component
public class MailService {
    private final JavaMailSender emailSender;
    private final JwtTokenizer jwtTokenizer;
    private final MailTimeout timeoutCollector;

    /**
     * Send a verification link to specified e-mail.
     * Token is valid for 10 min. {@link at.ac.tuwien.sepr.groupphase.backend.config.SecurityPropertiesConfig}.
     * E-Mail is sent in own thread.
     *
     * @param email E-mail to be verified
     */
    @Async
    public void sendVerificationMail(String email, String language, LocalDateTime regStamp) {
        if (timeoutCollector.hasTimeout(email)) {
            return;
        }
        timeoutCollector.addEmail(email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("animalhub.service@gmail.com");
        message.setTo(email);
        message.setSubject("Animalhub e-mail confirmation");
        message.setText("http://localhost:4200/#/register/confirm/" + jwtTokenizer.getActivationToken(email, language, regStamp));
        emailSender.send(message);
    }

    /**
     * Sends an email with a link to reset password of user.
     * Token is valid for 10 min. {@link at.ac.tuwien.sepr.groupphase.backend.config.SecurityPropertiesConfig}.
     * E-Mail is sent in own thread.
     *
     * @param email E-mail/username of user where password is needed to be reset
     * @param language Language used for the reset
     */
    @Async
    public void sendResetPasswordMail(String email, String language) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("animalhub.service@gmail.com");
        message.setTo(email);
        message.setSubject("Animal Hub reset password");
        message.setText("http://localhost:4200/#/password/reset/" + jwtTokenizer.getPasswordToken(email, language));
        emailSender.send(message);
    }
}
