package pl.pn.pnnotification.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class Utils {

    private Utils() {
    }

    public static List<String> getOnlyValidEmailsToNotify(List<String> allVotedEmails,
        List<String> emailsToNotifyFromSheet) {
        List<String> validEmails = new ArrayList<>();

        if (allVotedEmails == null || emailsToNotifyFromSheet == null) {
            log.error("Jedna z list jest nullem");
            log.error("Lista topki: {}", allVotedEmails);
            log.error("Lista notyfikacji: {}", emailsToNotifyFromSheet);
            log.error("Uwaga, zwracam pustą listę, aby nie wysłać wiadomości!");
            return new ArrayList<>();
        } else if (allVotedEmails.isEmpty() || emailsToNotifyFromSheet.isEmpty()) {
            log.error("Jedna z list jest pusta");
            log.error("Wielkość listy topki: {}", allVotedEmails.size());
            log.error("Wielkość listy notyfikacji: {}", emailsToNotifyFromSheet.size());
            log.error("Uwaga, zwracam pustą listę, aby nie wysłać wiadomości!");
            return new ArrayList<>();
        }

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

    public static void validateAllFieldsInApplicationProperties(Map<String, String> allValidVariables) {
        log.info("Weryfikuje zmienne z pliku application.properties");
        for (Map.Entry<String, String> entry : allValidVariables.entrySet()) {
            if(entry.getValue() == null || entry.getValue().isEmpty()) {
                log.error("UWAGA, błędna zmienna: {}", entry.getKey());
            }
        }
    }
}
