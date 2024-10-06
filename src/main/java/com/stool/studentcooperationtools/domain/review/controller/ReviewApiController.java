package com.stool.studentcooperationtools.domain.review.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.review.controller.response.ReviewFindResponse;
import com.stool.studentcooperationtools.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @GetMapping("/api/v1/parts/{partId}/review")
    public ApiResponse<ReviewFindResponse> findReviews(@PathVariable("partId") Long partId){
        ReviewFindResponse response = reviewService.findReview(partId);
        return ApiResponse.of(HttpStatus.OK,response);
    }

}
