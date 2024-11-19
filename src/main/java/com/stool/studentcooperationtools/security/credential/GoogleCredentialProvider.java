package com.stool.studentcooperationtools.security.credential;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.slides.v1.SlidesScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.Getter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Collections;
import java.util.List;

@Getter
@Component
public class GoogleCredentialProvider {
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final List<String> SCOPES = Collections.singletonList(SlidesScopes.PRESENTATIONS);

    private final String credentialsforupdateFilePath;

    private Credential credential;

    private HttpCredentialsAdapter credentialsAdapter;

    public GoogleCredentialProvider(@Value("${google.slides.credentials-forupdate-file-path}") String credentialsforupdateFilePath) throws IOException {
        this.credentialsforupdateFilePath = credentialsforupdateFilePath;
    }

    public void initializeCredential(String accessToken) throws IOException {
        this.credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(accessToken);
    }

    public void initializeCredentialAdapter() throws IOException {
        this.credentialsAdapter = (HttpCredentialsAdapter) getHttpRequestInitializer();
    }

    private HttpRequestInitializer getHttpRequestInitializer() {
        try(InputStream in = GoogleCredentialProvider.class.getResourceAsStream(credentialsforupdateFilePath)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                    .createScoped(List.of(DriveScopes.DRIVE_FILE));
            return new HttpCredentialsAdapter(
                    credentials);
        }catch (IOException e) {
            throw new BeanCreationException(e.getMessage(), e.getCause());
        }
    }

}