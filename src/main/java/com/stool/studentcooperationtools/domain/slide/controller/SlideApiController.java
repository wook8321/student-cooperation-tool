package com.stool.studentcooperationtools.domain.slide.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.slide.controller.response.SlideFindResponse;
import com.stool.studentcooperationtools.domain.slide.service.SlideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
public class SlideApiController {

    private final SlideService slideService;

    @GetMapping("/api/v1/presentation/{presentationId}/slides")
    public ApiResponse<SlideFindResponse> findSlides(@PathVariable("presentationId") Long presentationId){
        SlideFindResponse result = slideService.findSlides(presentationId);
        return ApiResponse.of(HttpStatus.OK,result);
    }

    @PostMapping("/api/v1/presentation/{presentationId}/slides")
    public ApiResponse<Boolean> updateSlides(@PathVariable("presentationId") Long presentationId, Credential credential) throws GeneralSecurityException, IOException {
        boolean result = slideService.updateSlides(presentationId, credential);
        return ApiResponse.of(HttpStatus.OK,result);
    }
}
