package pl.pn.pnnotification.google;

import java.util.List;

public interface GoogleSheetService {

    List<String> getMonthTopAllEmails();

    List<String> getEmailsToNotify();
}
