package pl.pn.pnnotification.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Utils {

    private Utils() {
    }

    public static List<String> getOnlyValidEmailsToNotify(List<String> allVotedEmails,
        List<String> emailsToNotifyFromSheet) {
        List<String> validEmails = new ArrayList<>();

        for (String email : emailsToNotifyFromSheet) {
            if (!allVotedEmails.contains(email)) {
                validEmails.add(email);
            }
        }

        printList(validEmails, "Finalna lista emaili do wysłania:");

        return validEmails;
    }

    public static void printList(List list, String message) {
        log.info(message);
        for (Object o : list) {
            log.info(o.toString());
        }
    }

    public static String insertLinkIntoSubject(String subject, String link) {
        String linkPlaceHolder = "<LINK>";
        return subject.replace(linkPlaceHolder, link);
    }
}
