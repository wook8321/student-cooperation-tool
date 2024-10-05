package com.stool.studentcooperationtools.domain.room.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomAddRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomPasswordValidRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomRemoveRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomTopicUpdateRequest;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomAddResponse;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomSearchResponse;
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

    @GetMapping("/api/v1/rooms/search")
    public ApiResponse<RoomSearchResponse> searchRoom(
            @RequestParam("title") String title,
            @RequestParam("page") int page){
        RoomSearchResponse response = roomService.searchRoom(title, page);
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @DeleteMapping("/api/v1/rooms")
    public ApiResponse<Boolean> removeRoom(@Valid @RequestBody RoomRemoveRequest request){
        Boolean result = roomService.removeRoom(request);
        return ApiResponse.of(HttpStatus.OK,result);
    }

    @PostMapping("/api/v1/rooms/valid-password")
    public ApiResponse<Boolean> validRoomPassword(@Valid @RequestBody RoomPasswordValidRequest request){
        Boolean result = roomService.validRoomPassword(request);
        return ApiResponse.of(HttpStatus.OK, result);
    }

    @PostMapping("/api/v1/rooms/topics")
    public ApiResponse<Boolean> updateRoomTopic(@Valid @RequestBody RoomTopicUpdateRequest request){
        Boolean result = roomService.updateRoomTopic(request);
        return ApiResponse.of(HttpStatus.OK,result);
    }
}
