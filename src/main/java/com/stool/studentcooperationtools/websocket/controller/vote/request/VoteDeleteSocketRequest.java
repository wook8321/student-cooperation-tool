package com.stool.studentcooperationtools.websocket.controller.vote.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteDeleteSocketRequest {

    private Long roomId;
    private Long voteId;

    @Builder
    private VoteDeleteSocketRequest(final Long roomId, final Long voteId) {
        this.roomId = roomId;
        this.voteId = voteId;
    }
}
