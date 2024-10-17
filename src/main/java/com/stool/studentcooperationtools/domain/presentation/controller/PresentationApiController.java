package com.stool.studentcooperationtools.domain.presentation.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.presentation.controller.response.PresentationFindResponse;
import com.stool.studentcooperationtools.domain.presentation.service.PresentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PresentationApiController {

    private final PresentationService presentationService;

    @GetMapping("/api/v1/rooms/{roomId}/presentations")
    public ApiResponse<PresentationFindResponse> findPresentation(@PathVariable("roomId") Long roomId){
        PresentationFindResponse response = presentationService.findPresentation(roomId);
        return ApiResponse.of(HttpStatus.OK,response);
    }

}
