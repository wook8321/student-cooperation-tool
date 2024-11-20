package com.stool.studentcooperationtools.domain.review.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDeleteRequest {

    @NotNull
    private Long reviewId;

    @Builder
    private ReviewDeleteRequest(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
