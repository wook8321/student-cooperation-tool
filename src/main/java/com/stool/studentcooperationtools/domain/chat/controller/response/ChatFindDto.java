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
    private Long userId;

    @Builder
    private ChatFindDto(final Long chatId, final LocalDate createdTime, final String nickName, final String profile, final String content, final Long userId) {
        this.chatId = chatId;
        this.createdTime = createdTime;
        this.nickName = nickName;
        this.profile = profile;
        this.content = content;
        this.userId = userId;
    }

    public static ChatFindDto of(Chat chat){
        return ChatFindDto.builder()
                .chatId(chat.getId())
                .createdTime(chat.getCreatedTime())
                .nickName(chat.getMember().getNickName())
                .profile(chat.getMember().getProfile())
                .content(chat.getContent())
                .userId(chat.getMember().getId())
                .build();
    }
}
