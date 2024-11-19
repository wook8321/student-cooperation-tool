package com.stool.studentcooperationtools.domain.room.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomPasswordValidRequest {

    @NotBlank
    private String password;

    @Builder
    private RoomPasswordValidRequest(final String password) {
        this.password = password;
    }
}
