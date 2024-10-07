package com.stool.studentcooperationtools.domain.slide.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.slide.controller.response.SlideFindResponse;
import com.stool.studentcooperationtools.domain.slide.service.SlideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SlideApiController {

    private final SlideService slideService;

    @GetMapping("/api/v1/presentations/{presentationId}/slides")
    public ApiResponse<SlideFindResponse> findSlides(@PathVariable("presentationId") Long presentationId){
        SlideFindResponse result = slideService.findSlides(presentationId);
        return ApiResponse.of(HttpStatus.OK,result);
    }
}
