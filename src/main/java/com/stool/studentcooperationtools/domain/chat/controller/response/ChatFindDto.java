package com.stool.studentcooperationtools.domain.chat.controller.response;

import com.stool.studentcooperationtools.domain.chat.Chat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ChatFindDto {

    private Long chatId;
    private LocalDate createdTime;
    private String nickName;
    private String profile;
    private String content;

    @Builder
    private ChatFindDto(final Long chatId, final LocalDate createdTime, final String nickName, final String profile, final String content) {
        this.chatId = chatId;
        this.createdTime = createdTime;
        this.nickName = nickName;
        this.profile = profile;
        this.content = content;
    }

    public static ChatFindDto of(Chat chat){
        return ChatFindDto.builder()
                .chatId(chat.getId())
                .createdTime(chat.getCreatedTime())
                .nickName(chat.getMember().getNickName())
                .profile(chat.getMember().getProfile())
                .content(chat.getContent())
                .build();
    }
}
