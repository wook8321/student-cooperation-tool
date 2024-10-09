package com.stool.studentcooperationtools.domain.presentation.controller.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PresentationFindResponse {

    private Long presentationId;
    private String presentationPath;
    private LocalDate updatedTime;

    @Builder
    private PresentationFindResponse(final Long presentationId, final String presentationPath, final LocalDate updatedTime) {
        this.presentationId = presentationId;
        this.presentationPath = presentationPath;
        this.updatedTime = updatedTime;
    }
}
