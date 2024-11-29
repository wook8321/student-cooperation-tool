package com.stool.studentcooperationtools.websocket.controller.vote;

import com.stool.studentcooperationtools.domain.vote.service.VoteService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.Utils.SimpleMessageSendingUtils;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.vote.request.VoteUpdateWebSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.vote.response.VoteUpdateWebSocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.VOTE_UPDATE;

@Controller
@RequiredArgsConstructor
public class VoteWebSocketController {

    private final VoteService voteService;
    private final SimpleMessageSendingUtils sendingUtils;

    @MessageMapping("/votes/update")
    public void updateVote(@Valid @RequestBody VoteUpdateWebSocketRequest request, SessionMember member){
        VoteUpdateWebSocketResponse response = voteService.updateVote(request,member);
        sendingUtils.convertAndSend(
                sendingUtils.createTopicDecisionSubUrl(request.getRoomId()),
                WebsocketResponse.of(VOTE_UPDATE,response)
        );
    }

}
