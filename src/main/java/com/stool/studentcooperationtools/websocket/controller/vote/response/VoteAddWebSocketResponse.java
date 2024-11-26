package com.stool.studentcooperationtools.websocket.controller.vote.response;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.vote.Vote;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteAddWebSocketResponse {

    private Long voteId;
    private Long topicId;
    private int voteNum;
    private Long memberId;

    @Builder
    private VoteAddWebSocketResponse(final Long voteId, final int voteNum, final Long topicId, final Long memberId) {
        this.voteId = voteId;
        this.topicId = topicId;
        this.voteNum = voteNum;
        this.memberId = memberId;
    }

    public static VoteAddWebSocketResponse of(final Vote vote, final Member member,final Topic topic){
        return VoteAddWebSocketResponse.builder()
                .voteId(vote.getId())
                .topicId(topic.getId())
                .voteNum(topic.getVotes().size())
                .memberId(member.getId())
                .build();
    }

}
