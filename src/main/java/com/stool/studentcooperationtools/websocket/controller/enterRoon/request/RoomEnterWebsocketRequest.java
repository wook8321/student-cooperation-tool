package com.stool.studentcooperationtools.websocket.controller.enterRoon.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomEnterWebsocketRequest {

    @NotNull
    private Long roomId;

    @Builder
    private RoomEnterWebsocketRequest(final Long roomId) {
        this.roomId = roomId;
    }
}
