package pl.pn.pnnotification.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pn.pnnotification.email.EmailServiceImpl;
import pl.pn.pnnotification.google.GoogleSheetUtils;
import pl.pn.pnnotification.utils.Utils;

import java.util.List;

@Component
@Slf4j
public class StartupRunner implements CommandLineRunner {

    private final GoogleSheetUtils googleSheetUtils;
    private final EmailServiceImpl emailService;

    @Autowired
    public StartupRunner(GoogleSheetUtils googleSheetUtils, EmailServiceImpl emailService) {
        this.googleSheetUtils = googleSheetUtils;
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) {
        log.info("Uruchomiono system wysylania powiadomień");
        List<String> allVotedEmails = googleSheetUtils.getMonthTopAllEmails();
        List<String> emailsToNotifyFromSheet = googleSheetUtils.getEmailsToNotify();

        List<String> finalEmailsToNotify = Utils.getOnlyValidEmailsToNotify(allVotedEmails, emailsToNotifyFromSheet);

        for(String email : finalEmailsToNotify) {
            log.info("TEST, wyslano do: " + email);
            //emailService.sendEmail("TEST", "Hello World!");
        }
    }
}
