package com.stool.studentcooperationtools.websocket.controller.topic;

import com.stool.studentcooperationtools.domain.topic.service.TopicService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.Utils.SimpleMessageSendingUtils;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.topic.request.TopicAddSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.topic.request.TopicDeleteSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.topic.response.TopicAddSocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.TOPIC_ADD;
import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.TOPIC_DELETE;

@Controller
@RequiredArgsConstructor
public class TopicWebsocketController {

    private final TopicService topicService;
    private final SimpleMessageSendingUtils sendingUtils;
    @MessageMapping("/topics/add")
    public void addTopic(@Valid @RequestBody TopicAddSocketRequest request, SessionMember member){
        TopicAddSocketResponse response = topicService.addTopic(request, member);
        sendingUtils.convertAndSend(
                sendingUtils.createTopicDecisionSubUrl(request.getRoomId()),
                WebsocketResponse.of(TOPIC_ADD,response)
        );
    }

    @MessageMapping("/topics/delete")
    public void deleteTopic(@Valid @RequestBody TopicDeleteSocketRequest request, SessionMember member){
        Boolean result = topicService.deleteTopic(request,member);
        sendingUtils.convertAndSend(
                sendingUtils.createTopicDecisionSubUrl(request.getRoomId()),
                WebsocketResponse.of(TOPIC_DELETE,result)
        );
    }

}
