package com.stool.studentcooperationtools.websocket.controller.topic.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicAddSocketRequest {

    @NotNull
    private Long roomId;

    @NotBlank
    private String topic;

    @Builder
    private TopicAddSocketRequest(final Long roomId, final String topic) {
        this.roomId = roomId;
        this.topic = topic;
    }

}
