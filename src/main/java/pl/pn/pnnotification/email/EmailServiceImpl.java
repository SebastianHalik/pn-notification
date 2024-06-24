package pl.pn.pnnotification.email;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class EmailServiceImpl implements EmailService {

    private final MailConfig config;

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(MailConfig config, JavaMailSender emailSender) {
        this.config = config;
        this.emailSender = emailSender;
    }

    public void sendEmail(String subject, String text, String emailTo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(config.getUsername());
            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(text);
            log.info("Wysyłanie emaila do usera: {}", emailTo);
            emailSender.send(message);
        }
        catch (MailAuthenticationException e) {
            log.error("Błąd weryfikacji - błędne hasło lub login do google");
            log.error("Jeśli widzisz tę wiadomość wiadomości nie zostały wysłane");
            log.error(e.getMessage(), e);
        }
    }
}