package com.stool.studentcooperationtools.websocket.controller.part.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartDeleteWebsocketResponse {

    private Long partId;

    @Builder
    private PartDeleteWebsocketResponse(final Long partId) {
        this.partId = partId;
    }

    public static PartDeleteWebsocketResponse of(final Long partId) {
        return PartDeleteWebsocketResponse.builder()
                .partId(partId)
                .build();
    }
}
