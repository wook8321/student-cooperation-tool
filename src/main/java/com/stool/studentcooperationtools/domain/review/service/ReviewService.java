package com.stool.studentcooperationtools.domain.review.service;

import com.stool.studentcooperationtools.domain.review.Review;
import com.stool.studentcooperationtools.domain.review.controller.request.ReviewAddRequest;
import com.stool.studentcooperationtools.domain.review.controller.response.ReviewAddResponse;
import com.stool.studentcooperationtools.domain.review.controller.response.ReviewFindResponse;
import com.stool.studentcooperationtools.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewFindResponse findReview(final Long partId) {
        List<Review> result = reviewRepository.findAllByPartIdOrderByIdDesc(partId);
        return ReviewFindResponse.of(result);
    }

    public ReviewAddResponse addReview(final ReviewAddRequest request) {
        return null;
    }
}
