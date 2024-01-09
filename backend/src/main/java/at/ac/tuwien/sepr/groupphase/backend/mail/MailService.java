package at.ac.tuwien.sepr.groupphase.backend.mail;

import at.ac.tuwien.sepr.groupphase.backend.config.LanguageConfig;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

/**
 * Component offering services to send e-mail.
 * Emails are sent in own threads.
 */
@RequiredArgsConstructor
@Component
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JavaMailSender emailSender;
    private final JwtTokenizer jwtTokenizer;
    private final MailTimeout timeoutCollector;
    private final LanguageConfig lang;

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
            LOGGER.warn(email + " has timout");
            return;
        }
        timeoutCollector.addEmail(email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("animalhub.service@gmail.com");
        message.setTo(email);
        message.setSubject("Animalhub e-mail confirmation");
        message.setText(lang.getTranslation(language + ".mail.verifyEmail") + "\nhttp://localhost:4200/#/register/confirm/"
            + jwtTokenizer.getActivationToken(email, language, regStamp));
        emailSender.send(message);
    }

    /**
     * Sends an email with a link to reset password of user.
     * Token is valid for 10 min. {@link at.ac.tuwien.sepr.groupphase.backend.config.SecurityPropertiesConfig}.
     * E-Mail is sent in own thread.
     *
     * @param email    E-mail/username of user where password is needed to be reset
     * @param language Language used for the reset
     */
    @Async
    public void sendResetPasswordMail(String email, String language) {
        if (timeoutCollector.hasTimeout(email)) {
            LOGGER.warn(email + " has timout");
            return;
        }
        timeoutCollector.addEmail(email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("animalhub.service@gmail.com");
        message.setTo(email);
        message.setSubject("Animal Hub reset password");
        message.setText(lang.getTranslation(language + ".mail.resetPwd") + "\nhttp://localhost:4200/#/password/reset/"
            + jwtTokenizer.getPasswordToken(email, language));
        emailSender.send(message);
    }

    /**
     * Asynchronously sends a contact email from a user to a shelter.
     * The email is sent in its own thread.
     *
     * @param to The email address of the shelter
     * @param from The email address of the user
     * @param name The name of the user
     * @param message The message from the user to the shelter
     */
    @Async
    public void sendShelterContactMail(String to, String from, String name,  String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject("Animal Hub Contact from: " + name);
        mailMessage.setText("Name: " + name + "\n" + "Message: " + message);
        emailSender.send(mailMessage);
    }
}
