package pl.pn.pnnotification.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Utils {

    private Utils() {
    }

    public static List<String> getOnlyValidEmailsToNotify(List<String> allEmails, List<String> alreadyVotedEmails) {
        List<String> validEmails = new ArrayList<>(allEmails);
        validEmails.removeAll(alreadyVotedEmails);

        printList(validEmails, "Finalna lista emaili do wysłania:");

        return validEmails;
    }

    public static void printList(List list, String message) {
        log.info(message);
        for (Object o : list) {
            log.info(o.toString());
        }
    }
}
