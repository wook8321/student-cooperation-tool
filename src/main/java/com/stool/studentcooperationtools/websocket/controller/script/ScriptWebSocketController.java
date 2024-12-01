package com.stool.studentcooperationtools.websocket.controller.script;

import com.stool.studentcooperationtools.domain.script.service.ScriptService;
import com.stool.studentcooperationtools.websocket.WebsocketMessageType;
import com.stool.studentcooperationtools.websocket.controller.Utils.SimpleMessageSendingUtils;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.script.request.ScriptUpdateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.script.response.ScriptUpdateSocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.SCRIPT_UPDATE;

@Controller
@RequiredArgsConstructor
public class ScriptWebSocketController {

    private final ScriptService scriptService;
    private final SimpleMessageSendingUtils sendingUtils;

    @MessageMapping("scripts/update")
    public void updateScript(@Valid @RequestBody ScriptUpdateSocketRequest request) {
        ScriptUpdateSocketResponse response = scriptService.updateScript(request);
        sendingUtils.convertAndSend(
                sendingUtils.createScriptManageSubUrl(request.getRoomId()),
                WebsocketResponse.of(SCRIPT_UPDATE, response)
        );
    }
}
