package com.stool.studentcooperationtools.domain.review.controller.response;

import com.stool.studentcooperationtools.domain.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewFindResponse {

    private int num;
    private List<ReviewFindDto> reviews;

    @Builder
    private ReviewFindResponse(final int num, final List<ReviewFindDto> reviews) {
        this.num = num;
        this.reviews = reviews;
    }

    public static ReviewFindResponse of(List<Review> reviews){
        return ReviewFindResponse.builder()
                .num(reviews.size())
                .reviews(
                        reviews.stream()
                                .map(ReviewFindDto::of)
                                .toList()
                )
                .build();
    }
}
