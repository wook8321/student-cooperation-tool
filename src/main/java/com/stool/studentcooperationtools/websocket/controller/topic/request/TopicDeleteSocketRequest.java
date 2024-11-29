package com.stool.studentcooperationtools.websocket.controller.topic.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicDeleteSocketRequest {

    @NotNull
    private Long topicId;

    @NotNull
    private Long roomId;

    @Builder
    private TopicDeleteSocketRequest(final Long topicId, final Long roomId) {
        this.topicId = topicId;
        this.roomId = roomId;
    }
}
