package com.stool.studentcooperationtools.websocket;

import com.stool.studentcooperationtools.websocket.controller.request.MessageTestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/chat")
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/send/message")
    public ResponseEntity<?> sendMessage(@RequestBody MessageTestRequest messageTestRequest){
        simpMessageSendingOperations.convertAndSend("/sub/chat/channel",messageTestRequest);
        return new ResponseEntity<String>(messageTestRequest.getMessage(), HttpStatus.OK);
    }

}
