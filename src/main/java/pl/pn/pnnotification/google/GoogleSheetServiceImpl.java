package pl.pn.pnnotification.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static pl.pn.pnnotification.google.GoogleAuthorize.*;
import static pl.pn.pnnotification.utils.Utils.printList;

@Service
@Slf4j
public class GoogleSheetServiceImpl implements GoogleSheetService {

    private static final String SHEET_NAME = "Liczba odpowiedzi: 1";

    @Value("${sheet.top.id}")
    private String topSheetId;

    @Value("${sheet.notification.id}")
    private String notificationSheetId;

    private final Sheets sheet = getSheet();

    private Sheets getSheet() {
        Sheets sheets = null;
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            final Credential credentials = getCredentials(HTTP_TRANSPORT);
            sheets = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
        } catch (GeneralSecurityException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return sheets;
    }

    @Override
    public List<String> getMonthTopAllEmails() {
        final List<String> allEmailsInThisMonth = new ArrayList<>();
        try {
            ValueRange response = sheet.spreadsheets().values()
                .get(topSheetId, SHEET_NAME)
                .execute();
            List<List<Object>> values = response.getValues();
            log.info("Pobrano listę emaili z topki");
            if (values == null || values.isEmpty()) {
                log.error("Brak wpisów w sheecie topki lub lista jest nullem");
            } else {
                for (List row : values) {
                    allEmailsInThisMonth.add(row.get(1).toString());
                }
                allEmailsInThisMonth.removeFirst(); //First cell is column's name
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        printList(allEmailsInThisMonth, "Lista osob, ktore zaglosowaly");
        return allEmailsInThisMonth;
    }

    @Override
    public List<String> getEmailsToNotify() {
        final List<String> allValidEmailsToNotify = new ArrayList<>();
        try {
            ValueRange response = sheet.spreadsheets().values()
                .get(notificationSheetId, SHEET_NAME)
                .execute();
            List<List<Object>> values = response.getValues();
            log.info("Pobrano listę emaili z arkusza notyfikacji");
            if (values == null || values.isEmpty()) {
                log.error("Brak wpisów w sheecie powiadomienia lub lista jest nullem");
            } else {
                for (List row : values) {
                    String email = row.get(1).toString();
                    String isToNotifyAsString = row.get(2).toString();
                    Boolean isToNotify = "TAK".equals(isToNotifyAsString);
                    if (Boolean.TRUE.equals(isToNotify)) {
                        allValidEmailsToNotify.add(email);
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        printList(allValidEmailsToNotify, "Lista osob, do powiadomienia");
        return allValidEmailsToNotify;
    }
}
