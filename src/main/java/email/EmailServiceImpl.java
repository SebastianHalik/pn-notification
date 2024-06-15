package email;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

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

    public void sendSimpleMessage(String subject, String text) {
        log.info("Wysyłanie emaila do usera: {}", config.getUsername());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(config.getUsername());
        message.setTo(config.getTo());
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @PostConstruct
    void init() {
        log.info("Uruchomiono system wysylania powiadomień");
        sendSimpleMessage("TEST", "Hello World!");
    }

    @Override
    public void sendMessageWithAttachment(
        String to, String subject, String text, String pathToAttachment) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(config.getUsername());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        FileSystemResource file
            = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Invoice", file);

        emailSender.send(message);
    }
}