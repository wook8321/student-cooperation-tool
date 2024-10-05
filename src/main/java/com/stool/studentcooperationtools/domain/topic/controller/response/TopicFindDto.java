package com.stool.studentcooperationtools.domain.topic.controller.response;


import com.stool.studentcooperationtools.domain.topic.Topic;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TopicFindDto {
    private Long topicId;
    private int voteCount;
    private String topic;

    @Builder
    private TopicFindDto(final Long topicId, final int voteCount, final String topic) {
        this.topicId = topicId;
        this.voteCount = voteCount;
        this.topic = topic;
    }

    public static TopicFindDto of(Topic topic){
        return TopicFindDto.builder()
                .topicId(topic.getId())
                .voteCount(topic.getVoteCount())
                .topic(topic.getTopic())
                .build();
    }
}
