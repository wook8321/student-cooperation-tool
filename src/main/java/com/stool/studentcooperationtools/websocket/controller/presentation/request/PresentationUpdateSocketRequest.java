package com.stool.studentcooperationtools.websocket.controller.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PresentationUpdateSocketRequest {

    @NotNull
    private Long roomId;

    @NotBlank
    private String presentationPath;

    @Builder
    public PresentationUpdateSocketRequest(Long roomId, String presentationPath) {
        this.roomId = roomId;
        this.presentationPath = presentationPath;
    }
}
