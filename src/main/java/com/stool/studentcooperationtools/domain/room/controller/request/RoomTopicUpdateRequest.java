package com.stool.studentcooperationtools.domain.room.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomTopicUpdateRequest {

    @NotNull
    private Long roomId;

    @NotNull
    private Long topicId;

    @Builder
    private RoomTopicUpdateRequest(final Long roomId, final Long topicId) {
        this.roomId = roomId;
        this.topicId = topicId;
    }
}
