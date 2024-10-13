package com.stool.studentcooperationtools.websocket.controller;

import com.stool.studentcooperationtools.websocket.controller.request.MessageTestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/send/message")
    public void sendMessage(@RequestBody MessageTestRequest messageTestRequest){
        simpMessageSendingOperations.convertAndSend("/sub/chat/channel",messageTestRequest);
    }

}
