package com.stool.studentcooperationtools.domain.topic.controller.response;


import com.stool.studentcooperationtools.domain.topic.Topic;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TopicFindDto {
    private Long topicId;
    private Long memberId;
    private String topic;
    private int voteNum;

    @Builder
    private TopicFindDto(final Long topicId,final int voteNum, final Long memberId, final String topic) {
        this.topicId = topicId;
        this.topic = topic;
        this.memberId = memberId;
        this.voteNum = voteNum;
    }

    public static TopicFindDto of(Topic topic){
        return TopicFindDto.builder()
                .topicId(topic.getId())
                .memberId(topic.getMember().getId())
                .voteNum(topic.getVoteNum())
                .topic(topic.getTopic())
                .build();
    }
}
