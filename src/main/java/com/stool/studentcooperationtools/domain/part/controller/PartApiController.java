package com.stool.studentcooperationtools.domain.part.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.part.controller.response.PartFindResponse;
import com.stool.studentcooperationtools.domain.part.service.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartApiController {

    private final PartService partService;

    @GetMapping("/api/v1/rooms/{roomId}/parts")
    public ApiResponse<PartFindResponse> findParts(@PathVariable("roomId") Long roomId){
        PartFindResponse response = partService.findParts(roomId);
        return ApiResponse.of(HttpStatus.OK, response);
    }

}
