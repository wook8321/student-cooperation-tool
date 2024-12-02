package com.stool.studentcooperationtools.websocket.controller.script;

import com.stool.studentcooperationtools.domain.script.service.ScriptService;
import com.stool.studentcooperationtools.websocket.WebsocketTestSupport;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.script.request.ScriptUpdateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.script.response.ScriptUpdateSocketResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.SCRIPT_UPDATE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ScriptWebSocketControllerTest extends WebsocketTestSupport {

    @MockBean
    private ScriptService scriptService;

    @Test
    @DisplayName("스크립트를 업데이트")
    void updateScript() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        Long roomId = 1L;
        Long scriptId = 1L;
        String ScriptUpdateSubUrl = "/sub/rooms/%d/scripts".formatted(roomId);
        ScriptUpdateSocketRequest request = ScriptUpdateSocketRequest.builder()
                .scriptId(scriptId)
                .script("s")
                .roomId(roomId)
                .build();
        ScriptUpdateSocketResponse response = ScriptUpdateSocketResponse.builder()
                .scriptId(scriptId)
                .script("s")
                .build();
        Mockito.when(scriptService.updateScript(Mockito.any(ScriptUpdateSocketRequest.class))).thenReturn(response);
        stompSession.subscribe(ScriptUpdateSubUrl, resultHandler);
        //when
        stompSession.send("/pub/scripts/update", request);
        WebsocketResponse result = resultHandler.get(1);
        //then
        assertThat(stompSession.isConnected()).isTrue();
        assertThat(result.getMessageType()).isEqualTo(SCRIPT_UPDATE);
        assertThat(result.getData()).isNotNull()
                .extracting("scriptId", "script")
                .containsExactly(1, "s");
    }

}