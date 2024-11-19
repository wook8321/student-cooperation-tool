package com.stool.studentcooperationtools.websocket.controller.chat.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatAddWebsocketRequest {

    @NotNull
    private Long roomId;
    @NotBlank
    private String content;

    @Builder
    public ChatAddWebsocketRequest(final Long roomId, final String content) {
        this.roomId = roomId;
        this.content = content;
    }

}
