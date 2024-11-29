package com.stool.studentcooperationtools.websocket.controller.vote.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteUpdateWebSocketRequest {

    @NotNull
    private Long roomId;
    @NotNull
    private Long topicId;

    @Builder
    private VoteUpdateWebSocketRequest(final Long roomId, final Long topicId) {
        this.roomId = roomId;
        this.topicId = topicId;
    }
}
