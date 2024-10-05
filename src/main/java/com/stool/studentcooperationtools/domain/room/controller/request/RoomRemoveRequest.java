package com.stool.studentcooperationtools.domain.room.controller.request;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomRemoveRequest {

    @NotNull
    private Long roomId;
    @Builder
    private RoomRemoveRequest(final Long roomId) {
        this.roomId = roomId;
    }
}
