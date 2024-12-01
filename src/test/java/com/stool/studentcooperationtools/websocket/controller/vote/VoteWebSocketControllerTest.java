package com.stool.studentcooperationtools.websocket.controller.vote;

import com.stool.studentcooperationtools.domain.vote.service.VoteService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.WebsocketTestSupport;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.vote.request.VoteUpdateWebSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.vote.response.VoteUpdateWebSocketResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.VOTE_UPDATE;
import static org.assertj.core.api.Assertions.assertThat;


class VoteWebSocketControllerTest extends WebsocketTestSupport {

    @MockBean
    VoteService voteService;

    @DisplayName("투표할 주제를 받아 투표를 추가 혹은 삭제한다..")
    @Test
    void addVote() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        Long roomId = 1L;
        String TopicDecisionSubUrl = "/sub/rooms/%d/topics".formatted(roomId);

        VoteUpdateWebSocketRequest request = VoteUpdateWebSocketRequest.builder()
                .topicId(1L)
                .roomId(roomId)
                .build();

        VoteUpdateWebSocketResponse response = VoteUpdateWebSocketResponse.builder()
                .voteNum(1)
                .topicId(1L)
                .build();

        Mockito.when(voteService.updateVote(Mockito.any(VoteUpdateWebSocketRequest.class),Mockito.any(SessionMember.class)))
                .thenReturn(response);

        stompSession.subscribe(TopicDecisionSubUrl,resultHandler);
        //when
        stompSession.send("/pub/votes/update",request);
        WebsocketResponse result = resultHandler.get(3);
        //then
        assertThat(stompSession.isConnected()).isTrue();
        assertThat(result.getMessageType()).isEqualTo(VOTE_UPDATE);
        assertThat(result.getData()).isNotNull();
    }
}