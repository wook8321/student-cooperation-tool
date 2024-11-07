package com.stool.studentcooperationtools.domain.chat.controller.response;

import com.stool.studentcooperationtools.domain.chat.Chat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class ChatFindResponse {

    private int num;
    private List<ChatFindDto> chats;
    private boolean hasNext;

    @Builder
    private ChatFindResponse(final int num, final List<ChatFindDto> chats,boolean hasNext) {
        this.num = num;
        this.chats = chats;
        this.hasNext = hasNext;
    }

    public static ChatFindResponse of(Slice<Chat> chats){
        return ChatFindResponse.builder()
                .num(chats.getSize())
                .chats(chats.getContent().stream()
                        .map(ChatFindDto::of)
                        .toList()
                )
                .hasNext(chats.hasNext())
                .build();
    }
}
