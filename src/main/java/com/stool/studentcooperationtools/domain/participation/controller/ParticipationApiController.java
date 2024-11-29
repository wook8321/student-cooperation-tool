package com.stool.studentcooperationtools.domain.participation.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.participation.controller.response.ParticipationViewResponse;
import com.stool.studentcooperationtools.domain.participation.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParticipationApiController {

    private final ParticipationService participationService;

    @GetMapping("/api/v1/rooms/{roomId}/participations")
    public ApiResponse<ParticipationViewResponse> getParticipationInRoom(@PathVariable("roomId") Long roomId){
        ParticipationViewResponse response = participationService.getParticipationIn(roomId);
        return ApiResponse.of(HttpStatus.OK,response);
    }
}
