package pl.pn.pnnotification.email;


public interface EmailService {

    void sendEmail(String subject, String text);
}
