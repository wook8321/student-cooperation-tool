package com.stool.studentcooperationtools.domain.room.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomEnterResponse {
    private Long leaderId;

    @Builder
    private RoomEnterResponse(final long leaderId) {
        this.leaderId = leaderId;
    }
}
