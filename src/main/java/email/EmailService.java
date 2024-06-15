package email;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendSimpleMessage(String subject, String text);

    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException;
}
