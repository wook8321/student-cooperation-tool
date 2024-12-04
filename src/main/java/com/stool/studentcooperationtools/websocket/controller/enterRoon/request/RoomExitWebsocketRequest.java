package com.stool.studentcooperationtools.websocket.controller.enterRoon.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomExitWebsocketRequest {

    @NotNull
    private Long roomId;

    @Builder
    private RoomExitWebsocketRequest(final Long roomId) {
        this.roomId = roomId;
    }
}
