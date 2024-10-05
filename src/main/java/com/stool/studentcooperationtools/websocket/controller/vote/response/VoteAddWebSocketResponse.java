package com.stool.studentcooperationtools.websocket.controller.vote.response;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.vote.Vote;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteAddWebSocketResponse {

    private Long voteId;
    private Long memberId;

    @Builder
    private VoteAddWebSocketResponse(final Long voteId, final Long memberId) {
        this.voteId = voteId;
        this.memberId = memberId;
    }

    public static VoteAddWebSocketResponse of(Vote vote, Member member){
        return VoteAddWebSocketResponse.builder()
                .voteId(vote.getId())
                .memberId(member.getId())
                .build();
    }

}
