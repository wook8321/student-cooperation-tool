package com.stool.studentcooperationtools.websocket.controller.chat;

import com.stool.studentcooperationtools.domain.chat.service.ChatService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.Utils.SimpleMessageSendingUtils;
import com.stool.studentcooperationtools.websocket.controller.chat.request.ChatAddWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.chat.response.ChatAddWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.CHAT_ADD;

@Controller
@RequiredArgsConstructor
public class ChatWebsocketController {

    private final ChatService chatService;
    private final SimpleMessageSendingUtils sendingUtils;
    @MessageMapping("/chats/add")
    public void addChat(@Valid @RequestBody ChatAddWebsocketRequest request, SessionMember member){
        ChatAddWebsocketResponse response = chatService.addChat(request,member);
        sendingUtils.convertAndSend(
                sendingUtils.createChatRoomSubUrl(request.getRoomId()),
                WebsocketResponse.of(CHAT_ADD,response)
        );
    }

}
