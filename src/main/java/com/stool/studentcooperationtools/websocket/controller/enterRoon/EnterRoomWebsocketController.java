package com.stool.studentcooperationtools.websocket.controller.enterRoon;

import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.Utils.SimpleMessageSendingUtils;
import com.stool.studentcooperationtools.websocket.controller.enterRoon.domain.Online;
import com.stool.studentcooperationtools.websocket.controller.enterRoon.repository.OnlineRepository;
import com.stool.studentcooperationtools.websocket.controller.enterRoon.request.RoomEnterWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.enterRoon.request.RoomExitWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.enterRoon.response.RoomEnterWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.enterRoon.response.RoomExitWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.ROOM_ENTER;
import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.ROOM_EXIT;

@RestController
@RequiredArgsConstructor
public class EnterRoomWebsocketController {

    private final SimpleMessageSendingUtils sendingUtils;
    private final OnlineRepository onlineRepository;

    @MessageMapping("/room/enter")
    public void enterRoom(@Valid @RequestBody RoomEnterWebsocketRequest request, SessionMember sessionMember){
        Online online = Online.of(sessionMember);
        List<Online> userProfiles = onlineRepository.putOnline(request.getRoomId(), online);
        sendingUtils.convertAndSend(
                sendingUtils.creatRoomEnterSubUrl(request.getRoomId()),
                WebsocketResponse.of(ROOM_ENTER, RoomEnterWebsocketResponse.of(userProfiles))
        );
    }

    @MessageMapping("/room/exit")
    public void exitRoom(@Valid @RequestBody RoomExitWebsocketRequest request, SessionMember sessionMember){
        Online online = Online.of(sessionMember);
        List<Online> userProfiles = onlineRepository.removeOnline(request.getRoomId(), online);
        sendingUtils.convertAndSend(
                sendingUtils.creatRoomEnterSubUrl(request.getRoomId()),
                WebsocketResponse.of(ROOM_EXIT, RoomExitWebsocketResponse.of(userProfiles))
        );
    }


}
