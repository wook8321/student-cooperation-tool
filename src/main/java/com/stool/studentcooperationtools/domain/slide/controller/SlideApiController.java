package com.stool.studentcooperationtools.domain.slide.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.slide.controller.response.SlideFindResponse;
import com.stool.studentcooperationtools.domain.slide.service.SlideService;
import com.stool.studentcooperationtools.security.credential.GoogleCredentialProvider;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SlideApiController {

    private final SlideService slideService;
    private final GoogleCredentialProvider credentialProvider;

    @GetMapping("/api/v1/presentation/{presentationId}/slides")
    public ApiResponse<SlideFindResponse> findSlides(@PathVariable("presentationId") Long presentationId){
        SlideFindResponse result = slideService.findSlides(presentationId);
        return ApiResponse.of(HttpStatus.OK,result);
    }

    @PostMapping("/api/v1/presentation/{presentationId}/slides")
    public ApiResponse<Boolean> updateSlides(@PathVariable("presentationId") Long presentationId, SessionMember member) {
        Credential credential = credentialProvider.getCredential();
        boolean result = slideService.updateSlides(presentationId, credential);
        return ApiResponse.of(HttpStatus.OK,result);
    }

}
