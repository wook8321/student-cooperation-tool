package com.stool.studentcooperationtools.domain.vote.response;

import com.stool.studentcooperationtools.domain.vote.Vote;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteFindDto {

    private Long memberId;
    private Long voteId;

    @Builder
    private VoteFindDto(final Long memberId, final Long voteId) {
        this.memberId = memberId;
        this.voteId = voteId;
    }

    public static VoteFindDto of(Vote vote) {
        return VoteFindDto.builder()
                .memberId(vote.getId())
                .voteId(vote.getVoter().getId())
                .build();
    }
}
