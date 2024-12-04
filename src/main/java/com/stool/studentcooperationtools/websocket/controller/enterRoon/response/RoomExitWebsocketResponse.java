package com.stool.studentcooperationtools.websocket.controller.enterRoon.response;

import com.stool.studentcooperationtools.websocket.controller.enterRoon.domain.Online;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomExitWebsocketResponse {

    private int onlineNum;
    private List<OnlineUserProfileDto> online;

    @Builder
    private RoomExitWebsocketResponse(final int onlineNum, final List<OnlineUserProfileDto> online) {
        this.onlineNum = onlineNum;
        this.online = online;
    }

    public static RoomExitWebsocketResponse of(final List<Online> userProfiles) {
        return RoomExitWebsocketResponse.builder()
                .onlineNum(userProfiles.size())
                .online(
                        userProfiles.stream()
                                .map(OnlineUserProfileDto::of)
                                .toList()
                )
                .build();
    }

}
