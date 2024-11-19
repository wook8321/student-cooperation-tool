package com.stool.studentcooperationtools.websocket.controller.chat.response;


import com.stool.studentcooperationtools.domain.chat.Chat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ChatAddWebsocketResponse {

    private LocalDate createdTime;
    private Long chatId;

    @Builder
    public ChatAddWebsocketResponse(final LocalDate createdTime, final Long chatId) {
        this.createdTime = createdTime;
        this.chatId = chatId;
    }

    public static ChatAddWebsocketResponse of(final Chat chat) {
        return ChatAddWebsocketResponse.builder()
                .chatId(chat.getId())
                .createdTime(chat.getCreatedTime())
                .build();
    }
}
