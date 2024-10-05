package com.stool.studentcooperationtools.domain.member.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberSearchResponse;
import com.stool.studentcooperationtools.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/friends")
    public ApiResponse<MemberSearchResponse> findFriends(){
        MemberSearchResponse response = memberService.findFriends();
        return ApiResponse.of(HttpStatus.OK,response);
    }

}
