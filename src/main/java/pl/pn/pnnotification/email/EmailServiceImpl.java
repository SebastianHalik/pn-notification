package pl.pn.pnnotification.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
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
        log.info("Wysyłanie emaila do usera: {}", emailTo);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(config.getUsername());
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}