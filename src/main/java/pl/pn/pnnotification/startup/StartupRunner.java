package pl.pn.pnnotification.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pn.pnnotification.email.EmailServiceImpl;
import pl.pn.pnnotification.google.GoogleSheetServiceImpl;
import pl.pn.pnnotification.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.pn.pnnotification.utils.Utils.insertLinkIntoSubject;
import static pl.pn.pnnotification.utils.Utils.validateAllFieldsInApplicationProperties;

@Component
@Slf4j
public class StartupRunner implements CommandLineRunner {

    private final GoogleSheetServiceImpl googleSheetServiceImpl;
    private final EmailServiceImpl emailService;

    @Value("${mail.title}")
    private String topic;

    @Value("${mail.text}")
    private String subject;

    @Value("${mail.link}")
    private String link;

    @Autowired
    public StartupRunner(GoogleSheetServiceImpl googleSheetServiceImpl, EmailServiceImpl emailService) {
        this.googleSheetServiceImpl = googleSheetServiceImpl;
        this.emailService = emailService;
    }

    private Map<String, String> addAllValidVariablesToMap() {
        Map<String, String> validVariables = new HashMap<>();
        validVariables.put("mail.title", topic);
        validVariables.put("mail.text", subject);
        validVariables.put("mail.link", link);
        validVariables.put("sheet.notification.id", googleSheetServiceImpl.getNotificationSheetId());
        validVariables.put("sheet.top.id", googleSheetServiceImpl.getTopSheetId());
        validVariables.put("mail.username", emailService.getConfig().getUsername());
        validVariables.put("mail.password", emailService.getConfig().getPassword());
        validVariables.put("mail.debug", emailService.getConfig().getDebug());
        return validVariables;
    }

    @Override
    public void run(String... args) {
        log.info("Uruchomiono system wysylania powiadomień");
        Map<String, String> allValidVariables = addAllValidVariablesToMap();
        validateAllFieldsInApplicationProperties(allValidVariables);

        List<String> allVotedEmails = googleSheetServiceImpl.downloadMonthTopAllEmails();
        List<String> emailsToNotifyFromSheet = googleSheetServiceImpl.downloadEmailsToNotify();

        List<String> finalEmailsToNotify = Utils.getOnlyValidEmailsToNotify(allVotedEmails, emailsToNotifyFromSheet);
        subject = insertLinkIntoSubject(subject, link);

        for (String email : finalEmailsToNotify) {
            emailService.sendEmail(topic, subject, email);
        }
        log.info("Wysyłka notyfikacji zakończona!");
    }
}
