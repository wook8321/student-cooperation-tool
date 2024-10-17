package com.stool.studentcooperationtools.domain.topic.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.topic.controller.response.TopicFindResponse;
import com.stool.studentcooperationtools.domain.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TopicApiController {

    private final TopicService topicService;

    @GetMapping("/api/v1/rooms/{roomId}/topics")
    public ApiResponse<TopicFindResponse> findTopics (@PathVariable("roomId") Long roomId){
        TopicFindResponse response = topicService.findTopics(roomId);
        return ApiResponse.of(HttpStatus.OK,response);
    }

}
