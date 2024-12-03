package com.stool.studentcooperationtools.domain.slide;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.slides.v1.Slides;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.slides.v1.SlidesScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.stool.studentcooperationtools.security.credential.GoogleCredentialProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SlidesFactory {

    public Slides createSlidesService(Credential credential) {
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            return new Slides.Builder(HTTP_TRANSPORT, GsonFactory.getDefaultInstance(), credential)
                    .setApplicationName("Google Slides API")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalArgumentException(e.getMessage(),e.getCause());
        }
    }


    public Drive createDriveService(HttpCredentialsAdapter credentialsAdapter) {
        return new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credentialsAdapter)
                .setApplicationName("Google Drive API")
                .build();
    }

    public Drive createDriveServicePerUser(Credential credential) {
        return new Drive.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("Google Drive API")
                .build();
    }
}