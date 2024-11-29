package com.stool.studentcooperationtools.websocket.controller.chat.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatDeleteWebsocketRequest {

    private Long roomId;
    private Long chatId;

    @Builder
    public ChatDeleteWebsocketRequest(final Long roomId, final Long chatId) {
        this.roomId = roomId;
        this.chatId = chatId;
    }
}
