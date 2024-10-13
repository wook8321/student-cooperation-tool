package com.stool.studentcooperationtools.websocket.controller.topic;

import com.stool.studentcooperationtools.domain.topic.service.TopicService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.topic.request.TopicAddSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.topic.response.TopicAddSocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class TopicWebsocketController {

    private final TopicService topicService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/topics/add")
    public void addTopic(@Valid @RequestBody TopicAddSocketRequest request, SessionMember member){
        TopicAddSocketResponse response = topicService.addTopic(request, member.getMemberSeq());
        simpMessageSendingOperations.convertAndSend(
                String.format("/sub/rooms/%d/topics",request.getRoomId()),
                response
        );
    }

}
