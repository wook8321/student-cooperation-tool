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
<<<<<<< HEAD
=======
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;

    @GetMapping("/api/v1/rooms")
<<<<<<< HEAD
    public ApiResponse<RoomsFindResponse> findRooms(@RequestParam("page") int page){
        RoomsFindResponse response = roomService.findRooms(page);
=======
    public ApiResponse<RoomsFindResponse> findRooms(SessionMember member, @RequestParam("page") int page){
        RoomsFindResponse response = roomService.findRooms(member, page);
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @PostMapping("/api/v1/rooms")
<<<<<<< HEAD
    public ApiResponse<RoomAddResponse> addRoom(@Valid @RequestBody RoomAddRequest request){
        RoomAddResponse response = roomService.addRoom(request);
=======
    public ApiResponse<RoomAddResponse> addRoom(SessionMember member, @Valid @RequestBody RoomAddRequest request){
        RoomAddResponse response = roomService.addRoom(member, request);
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
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
<<<<<<< HEAD
    public ApiResponse<Boolean> removeRoom(@Valid @RequestBody RoomRemoveRequest request){
        Boolean result = roomService.removeRoom(request);
=======
    public ApiResponse<Boolean> removeRoom(SessionMember member, @Valid @RequestBody RoomRemoveRequest request){
        Boolean result = roomService.removeRoom(member, request);
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
        return ApiResponse.of(HttpStatus.OK,result);
    }

    @PostMapping("/api/v1/rooms/valid-password")
<<<<<<< HEAD
    public ApiResponse<Boolean> validRoomPassword(@Valid @RequestBody RoomPasswordValidRequest request){
        Boolean result = roomService.validRoomPassword(request);
=======
    public ApiResponse<Boolean> validRoomPassword(SessionMember member, @Valid @RequestBody RoomPasswordValidRequest request){
        Boolean result = roomService.validRoomPassword(member, request);
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
        return ApiResponse.of(HttpStatus.OK, result);
    }

    @PostMapping("/api/v1/rooms/topics")
<<<<<<< HEAD
    public ApiResponse<Boolean> updateRoomTopic(@Valid @RequestBody RoomTopicUpdateRequest request){
        Boolean result = roomService.updateRoomTopic(request);
=======
    public ApiResponse<Boolean> updateRoomTopic(SessionMember member, @Valid @RequestBody RoomTopicUpdateRequest request){
        Boolean result = roomService.updateRoomTopic(member, request);
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
        return ApiResponse.of(HttpStatus.OK,result);
    }
}
