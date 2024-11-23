package com.stool.studentcooperationtools.websocket.controller.part.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartDeleteWebsocketRequest {

    @NotNull
    private Long roomId;
    @NotNull
    private Long partId;

    @Builder
    private PartDeleteWebsocketRequest(final Long roomId, final Long partId) {
        this.roomId = roomId;
        this.partId = partId;
    }

}
