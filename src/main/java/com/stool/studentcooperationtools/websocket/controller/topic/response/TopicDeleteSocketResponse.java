package com.stool.studentcooperationtools.websocket.controller.topic.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicDeleteSocketResponse {

    private Long topicId;
    @Builder
    private TopicDeleteSocketResponse(final Long topicId) {
        this.topicId = topicId;
    }

    public static TopicDeleteSocketResponse of(final Long topicId) {
        return TopicDeleteSocketResponse.builder()
                .topicId(topicId)
                .build();
    }
}
