package pl.pn.pnnotification.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static pl.pn.pnnotification.google.GoogleAuthorize.*;
import static pl.pn.pnnotification.utils.Utils.printList;

@Component
public class GoogleSheetUtils {

    private static final Logger log = LoggerFactory.getLogger(GoogleSheetUtils.class);
    @Value("${spring.sheet.top.id}")
    private String topSheetId;

    @Value("${spring.sheet.notification.id}")
    private String notificationSheetId;

    private final List<String> allEmailsInThisMonth = new ArrayList<>();
    private final String sheetName = "Liczba odpowiedzi: 1";
    private final Sheets sheet = getSheet();

    private GoogleSheetUtils() {
    }

    private Sheets getSheet() {
        Sheets sheets = null;
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            final Credential credentials = getCredentials(HTTP_TRANSPORT);
            sheets = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
        } catch (Exception e) {
            log.info("Błąd podczas łączenia się z GOOGLE API");
            log.error(e.getMessage(), e);
        }
        return sheets;
    }

    public String getSheetTitle() {
        String title = null;
        try {
            title = sheet.spreadsheets().get(topSheetId).execute().getProperties().getTitle();
        } catch (IOException e) {
            log.info("Błąd podczas pobierania tytuły sheeta");
            log.error(e.getMessage(), e);
        }
        return title;
    }

    public List<String> getMonthTopAllEmails() {
        try {
            ValueRange response = sheet.spreadsheets().values()
                .get(topSheetId, sheetName)
                .execute();
            List<List<Object>> values = response.getValues();
            log.info("Pobrano listę emaili z topki");
            if (values == null || values.isEmpty()) {
                log.error("NO DATA IN SHEET");
            } else {
                for (List row : values) {
                    allEmailsInThisMonth.add(row.get(1).toString());
                }
                allEmailsInThisMonth.removeFirst(); //First cell is column's name
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        printList(allEmailsInThisMonth, "Lista osob, ktore zaglosowaly");
        return allEmailsInThisMonth;
    }

    public List<String> getEmailsToNotify() {
        return new ArrayList<>();
    }
}
