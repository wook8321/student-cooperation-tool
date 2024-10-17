package com.stool.studentcooperationtools.domain.chat.controller.response;

import com.stool.studentcooperationtools.domain.chat.Chat;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatFindResponse {

    private int num;
    private List<ChatFindDto> chats;

    @Builder
    private ChatFindResponse(final int num, final List<ChatFindDto> chats) {
        this.num = num;
        this.chats = chats;
    }

    public static ChatFindResponse of(List<Chat> chats){
        return ChatFindResponse.builder()
                .num(chats.size())
                .chats(chats.stream()
                        .map(ChatFindDto::of)
                        .toList()
                )
                .build();
    }
}
