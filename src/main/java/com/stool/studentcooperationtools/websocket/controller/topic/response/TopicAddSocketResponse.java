package com.stool.studentcooperationtools.websocket.controller.topic.response;

import com.stool.studentcooperationtools.domain.topic.Topic;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicAddSocketResponse {

    private Long topicId;
    private String topic;
    private Long memberId;

    @Builder
    private TopicAddSocketResponse(final Long topicId, final String topic, final Long memberId) {
        this.topicId = topicId;
        this.topic = topic;
        this.memberId = memberId;
    }

    public static TopicAddSocketResponse of(Topic topic){
        return TopicAddSocketResponse.builder()
                .topicId(topic.getId())
                .memberId(topic.getMember().getId())
                .topic(topic.getTopic())
                .build();
    }
}
