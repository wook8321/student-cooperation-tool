package com.stool.studentcooperationtools.websocket.controller.vote;

import com.stool.studentcooperationtools.domain.vote.service.VoteService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.Utils.SimpleMessageSendingUtils;
import com.stool.studentcooperationtools.websocket.controller.vote.request.VoteAddWebSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.vote.request.VoteDeleteSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.vote.response.VoteAddWebSocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class VoteWebSocketController {

    private final VoteService voteService;
    private final SimpleMessageSendingUtils sendingUtils;

    @MessageMapping("/votes/add")
    public void addVote(@Valid @RequestBody VoteAddWebSocketRequest request, SessionMember member){
        VoteAddWebSocketResponse response = voteService.addVote(request, member);
        sendingUtils.convertAndSend(sendingUtils.createTopicDecisionSubUrl(request.getRoomId()),response);
    }

    @MessageMapping("/votes/delete")
    public void deleteVote(@Valid @RequestBody VoteDeleteSocketRequest request){
        Boolean result = voteService.deleteVote(request.getVoteId());
        sendingUtils.convertAndSend(sendingUtils.createTopicDecisionSubUrl(request.getRoomId()),result);
    }

}
