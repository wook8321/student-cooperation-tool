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
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;

    @GetMapping("/api/v1/rooms")
    public ApiResponse<RoomsFindResponse> findRooms(SessionMember member, @RequestParam("page") int page){
        RoomsFindResponse response = roomService.findRooms(member, page);
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @PostMapping("/api/v1/rooms")
    public ApiResponse<RoomAddResponse> addRoom(SessionMember member, @Valid @RequestBody RoomAddRequest request){
        RoomAddResponse response = roomService.addRoom(member, request);
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
    public ApiResponse<Boolean> removeRoom(SessionMember member, @Valid @RequestBody RoomRemoveRequest request){
        Boolean result = roomService.removeRoom(member, request);
        return ApiResponse.of(HttpStatus.OK,result);
    }

    @PostMapping("/api/v1/rooms/valid-password")
    public ApiResponse<Boolean> validRoomPassword(SessionMember member, @Valid @RequestBody RoomPasswordValidRequest request){
        Boolean result = roomService.validRoomPassword(member, request);
        return ApiResponse.of(HttpStatus.OK, result);
    }

    @PostMapping("/api/v1/rooms/topics")
    public ApiResponse<Boolean> updateRoomTopic(SessionMember member, @Valid @RequestBody RoomTopicUpdateRequest request){
        Boolean result = roomService.updateRoomTopic(member, request);
        return ApiResponse.of(HttpStatus.OK,result);
    }
}
