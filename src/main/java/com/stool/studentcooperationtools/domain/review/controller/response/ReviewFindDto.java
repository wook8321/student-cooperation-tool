package com.stool.studentcooperationtools.domain.review.controller.response;

import com.stool.studentcooperationtools.domain.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReviewFindDto {

    private Long reviewId;
    private String content;
    private Long memberId;
    private String nickName;
    private String profile;
    private LocalDate createdTime;

    @Builder
    private ReviewFindDto(final Long reviewId, final String content,
                         final Long memberId, final String nickName,
                         final String profile, final LocalDate createdTime) {
        this.reviewId = reviewId;
        this.content = content;
        this.memberId = memberId;
        this.nickName = nickName;
        this.profile = profile;
        this.createdTime = createdTime;
    }

    public static ReviewFindDto of(Review review) {
        return ReviewFindDto.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .memberId(review.getMember().getId())
                .nickName(review.getMember().getNickName())
                .profile(review.getMember().getProfile())
                .createdTime(review.getCreatedTime())
                .build();
    }
}
