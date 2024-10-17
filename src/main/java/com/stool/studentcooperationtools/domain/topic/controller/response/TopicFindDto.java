package com.stool.studentcooperationtools.domain.topic.controller.response;


import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.vote.response.VoteFindDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TopicFindDto {
    private Long topicId;
<<<<<<< HEAD
=======
    private Long memberId;
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
    private String topic;
    private int voteNum;
    private List<VoteFindDto> votes;

    @Builder
<<<<<<< HEAD
    private TopicFindDto(final Long topicId,final int voteNum, final List<VoteFindDto> votes, final String topic) {
        this.topicId = topicId;
        this.topic = topic;
=======
    private TopicFindDto(final Long topicId,final int voteNum, final List<VoteFindDto> votes,final Long memberId, final String topic) {
        this.topicId = topicId;
        this.topic = topic;
        this.memberId = memberId;
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
        this.voteNum = voteNum;
        this.votes = votes;
    }

    public static TopicFindDto of(Topic topic){
        return TopicFindDto.builder()
                .topicId(topic.getId())
<<<<<<< HEAD
=======
                .memberId(topic.getMember().getId())
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
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
