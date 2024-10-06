package com.stool.studentcooperationtools.domain.review.controller.response;

import com.stool.studentcooperationtools.domain.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewAddResponse {

    private Long reviewId;
    private String content;
    private String nickName;
    private String profile;
    private LocalDate createdTime;

    @Builder
    private ReviewAddResponse(final Long reviewId, final String content,
                             final String nickName, final String profile,
                             final LocalDate createdTime) {
        this.reviewId = reviewId;
        this.content = content;
        this.nickName = nickName;
        this.profile = profile;
        this.createdTime = createdTime;
    }

    public static ReviewAddResponse of(Review review){
        return ReviewAddResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .nickName(review.getMember().getNickName())
                .profile(review.getMember().getProfile())
                .createdTime(review.getCreatedTime())
                .build();
    }

}
