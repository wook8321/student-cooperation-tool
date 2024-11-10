package com.stool.studentcooperationtools.websocket.controller.part.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartUpdateWebsocketRequest {

    @NotNull
    private Long roomId;
    @NotNull
    private Long partId;
    @NotBlank
    private String partName;
    @NotNull
    private Long memberId;

    @Builder
    private PartUpdateWebsocketRequest(final Long roomId, final Long partId, final String partName, final Long memberId) {
        this.roomId = roomId;
        this.partId = partId;
        this.partName = partName;
        this.memberId = memberId;
    }
}
