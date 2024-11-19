package com.stool.studentcooperationtools.domain.review.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.review.controller.request.ReviewAddRequest;
import com.stool.studentcooperationtools.domain.review.controller.request.ReviewDeleteRequest;
import com.stool.studentcooperationtools.domain.review.controller.response.ReviewAddResponse;
import com.stool.studentcooperationtools.domain.review.controller.response.ReviewFindResponse;
import com.stool.studentcooperationtools.domain.review.service.ReviewService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @GetMapping("/api/v1/parts/{partId}/review")
    public ApiResponse<ReviewFindResponse> findReviews(@PathVariable("partId") Long partId){
        ReviewFindResponse response = reviewService.findReview(partId);
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @PostMapping("/api/v1/parts/review")
    public ApiResponse<ReviewAddResponse> addReviews(
            @Valid @RequestBody ReviewAddRequest request, SessionMember sessionMember
    ){
        ReviewAddResponse response = reviewService.addReview(request,sessionMember);
        return ApiResponse.of(HttpStatus.OK, response);
    }

    @DeleteMapping("/api/v1/parts/review")
    public ApiResponse<Boolean> deleteReview(
            @Valid @RequestBody ReviewDeleteRequest request, SessionMember sessionMember
    ){
        Boolean result = reviewService.deleteReview(request,sessionMember);
        return ApiResponse.of(HttpStatus.OK,result);
    }

}
