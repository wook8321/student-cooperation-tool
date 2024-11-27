package com.stool.studentcooperationtools.websocket.controller.chat.response;


import com.stool.studentcooperationtools.domain.chat.Chat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ChatAddWebsocketResponse {

    private Long memberId;
    private String content;
    private Long chatId;
    private String nickname;

    @Builder
    public ChatAddWebsocketResponse(final Long memberId, final Long chatId, final String content, final String nickname) {
        this.memberId = memberId;
        this.content = content;
        this.chatId = chatId;
        this.nickname = nickname;
    }

    public static ChatAddWebsocketResponse of(final Chat chat) {
        return ChatAddWebsocketResponse.builder()
                .memberId(chat.getMember().getId())
                .chatId(chat.getId())
                .content(chat.getContent())
                .nickname(chat.getMember().getNickName())
                .build();
    }
}
