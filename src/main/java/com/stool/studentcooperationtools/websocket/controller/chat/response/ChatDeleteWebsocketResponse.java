package com.stool.studentcooperationtools.websocket.controller.chat.response;

import lombok.Getter;

@Getter
public class ChatDeleteWebsocketResponse {

    private Long chatId;

    public ChatDeleteWebsocketResponse(final Long chatId) {
        this.chatId = chatId;
    }
}
