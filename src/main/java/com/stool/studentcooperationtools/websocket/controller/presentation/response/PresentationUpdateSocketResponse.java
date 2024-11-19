package com.stool.studentcooperationtools.websocket.controller.presentation.response;

import com.stool.studentcooperationtools.domain.presentation.Presentation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PresentationUpdateSocketResponse {

    private Long presentationId;
    private String presentationPath;

    @Builder
    public PresentationUpdateSocketResponse(Long presentationId, String presentationPath) {
        this.presentationId = presentationId;
        this.presentationPath = presentationPath;
    }

    public static PresentationUpdateSocketResponse of(Presentation presentation) {
        return PresentationUpdateSocketResponse.builder()
                .presentationId(presentation.getId())
                .presentationPath(presentation.getPresentationPath())
                .build();
    }

}
