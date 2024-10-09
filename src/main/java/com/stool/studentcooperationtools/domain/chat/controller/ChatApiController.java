package com.stool.studentcooperationtools.domain.chat.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.chat.controller.response.ChatFindResponse;
import com.stool.studentcooperationtools.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    @GetMapping("/api/v1/rooms/{roomId}/chats")
    public ApiResponse<ChatFindResponse> findChats(@PathVariable("roomId") Long roomId){
        ChatFindResponse response = chatService.findChats(roomId);
        return ApiResponse.of(HttpStatus.OK,response);
    }

}
