package com.stool.studentcooperationtools.websocket.controller.presentation;

import com.google.api.client.auth.oauth2.Credential;
import com.google.auth.http.HttpCredentialsAdapter;
import com.stool.studentcooperationtools.domain.presentation.service.PresentationService;
import com.stool.studentcooperationtools.security.credential.GoogleCredentialProvider;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.Utils.SimpleMessageSendingUtils;
import com.stool.studentcooperationtools.websocket.controller.presentation.request.PresentationCreateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.presentation.request.PresentationUpdateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.presentation.response.PresentationUpdateSocketResponse;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.PRESENTATION_CREATE;
import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.PRESENTATION_UPDATE;

@Controller
@RequiredArgsConstructor
public class PresentationWebSocketController {

    private final PresentationService presentationService;
    private final SimpleMessageSendingUtils sendingUtils;
    private final GoogleCredentialProvider credentialProvider;

    @MessageMapping("presentation/update")
    public void updatePresentation(@Valid @RequestBody PresentationUpdateSocketRequest request, SessionMember member) {
        PresentationUpdateSocketResponse response = presentationService.updatePresentation(request, member);
        sendingUtils.convertAndSend(
                sendingUtils.createPresentationManageSubUrl(request.getRoomId()),
                WebsocketResponse.of(PRESENTATION_UPDATE, response)
        );
    }

    @MessageMapping("presentation/create")
    public void createPresentation(@Valid @RequestBody PresentationCreateSocketRequest request, SessionMember member) {
        Credential credential = credentialProvider.getCredential();
        PresentationUpdateSocketResponse response = presentationService.createPresentation(request, credential, member);
        sendingUtils.convertAndSend(
                sendingUtils.createPresentationManageSubUrl(request.getRoomId()),
                WebsocketResponse.of(PRESENTATION_CREATE, response)
        );

    }
}
