package com.stool.studentcooperationtools.websocket.controller.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PresentationCreateSocketRequest {

    @NotNull
    private Long roomId;

    private String presentationName;

    @Builder
    public PresentationCreateSocketRequest(Long roomId, String presentationName) {
        this.roomId = roomId;
        this.presentationName = presentationName;
    }
}
