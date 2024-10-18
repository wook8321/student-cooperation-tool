package com.stool.studentcooperationtools.websocket.controller.vote;

import com.stool.studentcooperationtools.domain.vote.service.VoteService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.CustomSessionHandlerAdapter;
import com.stool.studentcooperationtools.websocket.WebsocketTestSupport;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.vote.request.VoteAddWebSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.vote.request.VoteDeleteSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.vote.response.VoteAddWebSocketResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.*;
import static org.assertj.core.api.Assertions.assertThat;


class VoteWebSocketControllerTest extends WebsocketTestSupport {

    @MockBean
    VoteService voteService;

    @DisplayName("투표할 주제를 받아 투표를 추가한다.")
    @Test
    void addVote() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        Long roomId = 1L;
        String TopicDecisionSubUrl = "/sub/rooms/%d/topics".formatted(roomId);
        CustomSessionHandlerAdapter<WebsocketResponse> handler =
                new CustomSessionHandlerAdapter<>(WebsocketResponse.class);

        VoteAddWebSocketRequest request =VoteAddWebSocketRequest.builder()
                .topicId(1L)
                .roomId(roomId)
                .build();

        VoteAddWebSocketResponse response = VoteAddWebSocketResponse.builder()
                .voteId(1L)
                .memberId(1L)
                .build();

        Mockito.when(voteService.addVote(Mockito.any(VoteAddWebSocketRequest.class),Mockito.any(SessionMember.class)))
                .thenReturn(response);

        stompSession.subscribe(TopicDecisionSubUrl,handler);
        //when
        stompSession.send("/pub/votes/add",request);
        WebsocketResponse result = handler.get(3);
        //then
        assertThat(stompSession.isConnected()).isTrue();
        assertThat(result.getMessageType()).isEqualTo(VOTE_ADD);
        assertThat(result.getData()).isNotNull()
                .extracting("voteId","memberId")
                .containsExactly(1,1);
    }

    @DisplayName("삭제할 투표 정보를 받아 투표를 삭제한다.")
    @Test
    void deleteVote() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        Long roomId = 1L;
        String TopicDecisionSubUrl = "/sub/rooms/%d/topics".formatted(roomId);
        CustomSessionHandlerAdapter<WebsocketResponse> handler =
                new CustomSessionHandlerAdapter<>(WebsocketResponse.class);

        VoteDeleteSocketRequest request =VoteDeleteSocketRequest.builder()
                .voteId(1L)
                .roomId(roomId)
                .build();

        Mockito.when(voteService.deleteVote(Mockito.anyLong(),Mockito.any(SessionMember.class)))
                .thenReturn(true);

        stompSession.subscribe(TopicDecisionSubUrl,handler);
        //when
        stompSession.send("/pub/votes/delete",request);
        WebsocketResponse<Boolean> result = handler.get(3);
        //then
        assertThat(stompSession.isConnected()).isTrue();
        assertThat(result.getMessageType()).isEqualTo(VOTE_DELETE);
        assertThat(result.getData()).isTrue();
    }
}