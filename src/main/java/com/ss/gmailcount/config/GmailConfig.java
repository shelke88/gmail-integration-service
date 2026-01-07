package com.ss.gmailcount.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Configuration
public class GmailConfig {

    @Bean
    public Gmail gmail() throws Exception {

        //Read credentials from the file.
        InputStream in = new ClassPathResource("secrets.json").getInputStream();

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(
                        JacksonFactory.getDefaultInstance(),
                        new InputStreamReader(in)
                );

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JacksonFactory.getDefaultInstance(),
                        clientSecrets,
                        List.of(GmailScopes.GMAIL_READONLY)
                )
                .setAccessType("offline")
                .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                .build();

        Credential credential =
                new AuthorizationCodeInstalledApp(
                        flow,
                        new LocalServerReceiver()
                ).authorize("user");

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        )
        .setApplicationName("Gmail Counter")
        .build();
    }
}
