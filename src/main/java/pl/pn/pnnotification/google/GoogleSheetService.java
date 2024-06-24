package pl.pn.pnnotification.google;

import java.util.List;

public interface GoogleSheetService {

    List<String> downloadMonthTopAllEmails();

    List<String> downloadEmailsToNotify();
}
