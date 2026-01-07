package com.ss.gmailcount.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GmailService {

    private final Gmail gmail;

    public GmailService(Gmail gmail) {
        this.gmail = gmail;
    }

    public long countEmails(String senderEmail) throws IOException {

        String query = "from:" + senderEmail;
        long count = 0;

        ListMessagesResponse response =
                gmail.users().messages()
                        .list("me")
                        .setQ(query)
                        .execute();

        while (response.getMessages() != null) {
            count += response.getMessages().size();

            if (response.getNextPageToken() == null) {
                break;
            }

            response = gmail.users().messages()
                    .list("me")
                    .setQ(query)
                    .setPageToken(response.getNextPageToken())
                    .execute();
        }

        return count;
    }
}
