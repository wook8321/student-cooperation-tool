package com.stool.studentcooperationtools.domain.topic.controller.response;


import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.vote.response.VoteFindDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TopicFindDto {
    private Long topicId;
    private Long memberId;
    private String topic;
    private int voteNum;
    private List<VoteFindDto> votes;

    @Builder
    private TopicFindDto(final Long topicId,final int voteNum, final List<VoteFindDto> votes,final Long memberId, final String topic) {
        this.topicId = topicId;
        this.topic = topic;
        this.memberId = memberId;
        this.voteNum = voteNum;
        this.votes = votes;
    }

    public static TopicFindDto of(Topic topic){
        return TopicFindDto.builder()
                .topicId(topic.getId())
                .memberId(topic.getMember().getId())
                .voteNum(topic.getVotes().size())
                .votes(
                        topic.getVotes().stream()
                        .map(VoteFindDto::of)
                                .toList()
                )
                .topic(topic.getTopic())
                .build();
    }
}
