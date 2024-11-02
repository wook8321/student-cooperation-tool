package com.stool.studentcooperationtools.websocket.controller.presentation;

import com.stool.studentcooperationtools.domain.presentation.service.PresentationService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.WebsocketTestSupport;
import com.stool.studentcooperationtools.websocket.controller.presentation.request.PresentationUpdateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.presentation.response.PresentationUpdateSocketResponse;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.PRESENTATION_UPDATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PresentationWebSocketControllerTest extends WebsocketTestSupport {

    @MockBean
    private PresentationService presentationService;

    @Test
    @DisplayName("방의 발표자료를 업데이트")
    void updatePresentation() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        Long roomId = 1L;
        String PresentationUpdateSubUrl = "/sub/rooms/%d/presentation".formatted(roomId);
        PresentationUpdateSocketRequest request = PresentationUpdateSocketRequest.builder()
                .presentationPath("path")
                .roomId(roomId)
                .build();
        PresentationUpdateSocketResponse response = PresentationUpdateSocketResponse.builder()
                        .presentationPath("path")
                        .presentationId(1L)
                        .build();
        Mockito.when(presentationService.updatePresentation(Mockito.any(PresentationUpdateSocketRequest.class),Mockito.any(SessionMember.class)))
                .thenReturn(response);

        stompSession.subscribe(PresentationUpdateSubUrl,resultHandler);
        //when
        stompSession.send("/pub/presentation/update",request);
        WebsocketResponse result = resultHandler.get(1);
        //then
        assertThat(stompSession.isConnected()).isTrue();
        assertThat(result.getMessageType()).isEqualTo(PRESENTATION_UPDATE);
        assertThat(result.getData()).isNotNull()
                .extracting("presentationId","presentationPath")
                .containsExactly(1,"path");
    }
}