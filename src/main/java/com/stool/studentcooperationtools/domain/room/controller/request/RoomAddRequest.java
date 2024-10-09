package com.stool.studentcooperationtools.domain.room.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomAddRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String password;

    private List<Long> participation;

    @Builder
    private RoomAddRequest(final String title, final String password, final List<Long> participation) {
        this.title = title;
        this.password = password;
        this.participation = participation;
    }

}
