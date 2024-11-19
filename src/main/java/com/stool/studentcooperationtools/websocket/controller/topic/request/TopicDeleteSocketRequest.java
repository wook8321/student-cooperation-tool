package com.stool.studentcooperationtools.websocket.controller.topic.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicDeleteSocketRequest {

    private Long topicId;
    private Long roomId;

    @Builder
    private TopicDeleteSocketRequest(final Long topicId, final Long roomId) {
        this.topicId = topicId;
        this.roomId = roomId;
    }
}
