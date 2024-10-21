package com.stool.studentcooperationtools.domain.room.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomPasswordValidRequest {

    @NotNull
    private Long roomId;
    @NotBlank
    private String password;

    @Builder
    private RoomPasswordValidRequest(final String password, final Long roomId) {
        this.password = password;
        this.roomId = roomId;
    }
}
