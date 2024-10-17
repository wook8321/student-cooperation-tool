package com.stool.studentcooperationtools.domain.room.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomAddResponse {
    private Long roomId;
    private String title;
    @Builder
    private RoomAddResponse(final Long roomId, final String title) {
        this.roomId = roomId;
        this.title = title;
    }
}
