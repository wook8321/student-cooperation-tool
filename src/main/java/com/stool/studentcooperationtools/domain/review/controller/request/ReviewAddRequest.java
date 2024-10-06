package com.stool.studentcooperationtools.domain.review.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewAddRequest {

    @NotNull
    private Long partId;

    @NotBlank
    private String content;

    @Builder
    private ReviewAddRequest(final Long partId, final String content) {
        this.partId = partId;
        this.content = content;
    }

}
