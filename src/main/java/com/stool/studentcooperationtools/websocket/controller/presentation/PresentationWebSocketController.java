package com.stool.studentcooperationtools.websocket.controller.presentation;

import com.stool.studentcooperationtools.domain.presentation.service.PresentationService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.Utils.SimpleMessageSendingUtils;
import com.stool.studentcooperationtools.websocket.controller.presentation.request.PresentationUpdateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.presentation.response.PresentationUpdateSocketResponse;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.PRESENTATION_UPDATE;

@Controller
@RequiredArgsConstructor
public class PresentationWebSocketController {

    private final PresentationService presentationService;
    private final SimpleMessageSendingUtils sendingUtils;

    @MessageMapping("presentation/update")
    public void updatePresentation(@Valid @RequestBody PresentationUpdateSocketRequest request, SessionMember member) {
        PresentationUpdateSocketResponse response = presentationService.updatePresentation(request, member);
        sendingUtils.convertAndSend(
                sendingUtils.createPresentationManageSubUrl(request.getRoomId()),
                WebsocketResponse.of(PRESENTATION_UPDATE, response)
        );
    }
}
