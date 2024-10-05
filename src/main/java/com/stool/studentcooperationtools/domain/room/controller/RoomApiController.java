package com.stool.studentcooperationtools.domain.room.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomAddRequest;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomAddResponse;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomsFindResponse;
import com.stool.studentcooperationtools.domain.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;

    @GetMapping("/api/v1/rooms")
    public ApiResponse<RoomsFindResponse> findRooms(@RequestParam("page") int page){
        RoomsFindResponse response = roomService.findRooms(page);
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @PostMapping("/api/v1/rooms")
    public ApiResponse<RoomAddResponse> addRoom(@Valid @RequestBody RoomAddRequest request){
        RoomAddResponse response = roomService.addRoom(request);
        return ApiResponse.of(HttpStatus.OK,response);
    }
}
