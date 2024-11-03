package com.stool.studentcooperationtools.security.credential;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.slides.v1.SlidesScopes;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Collections;
import java.util.List;

@Getter
@Component
public class GoogleCredentialProvider {
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final List<String> SCOPES = Collections.singletonList(SlidesScopes.PRESENTATIONS_READONLY);

    private final String credentialsFilePath;

    private final String tokensDirectoryPath;

    private final String userId;

    private Credential credential;

    public GoogleCredentialProvider(@Value("${google.slides.credentials-file-path}") String credentialsFilePath
    ,  @Value("${google.slides.tokens-directory-path}") String tokensDirectoryPath,  @Value("${google.slides.user-id}") String userId) throws IOException {
        this.credentialsFilePath = credentialsFilePath;
        this.tokensDirectoryPath = tokensDirectoryPath;
        this.userId = userId;
        this.credential = authorize();
    }

    private Credential authorize() throws IOException {
        InputStream in = GoogleCredentialProvider.class.getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensDirectoryPath)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(userId);
    }

}