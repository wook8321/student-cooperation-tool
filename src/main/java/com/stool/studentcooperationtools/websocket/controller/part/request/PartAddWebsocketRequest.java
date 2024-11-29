package com.stool.studentcooperationtools.websocket.controller.part.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PartAddWebsocketRequest {

    @NotNull
    private Long roomId;
    @NotBlank
    private String partName;
    @NotNull
    private Long memberId;

    @Builder
    private PartAddWebsocketRequest(final Long roomId, final String partName,final Long memberId) {
        this.roomId = roomId;
        this.partName = partName;
        this.memberId = memberId;
    }

}
