package com.stool.studentcooperationtools.domain.member.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.member.controller.request.MemberAddRequest;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberFindResponse;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberSearchResponse;
import com.stool.studentcooperationtools.domain.member.service.MemberService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/friends")
    public ApiResponse<MemberFindResponse> findFriends(SessionMember member){
        MemberFindResponse response = memberService.findFriends(member);
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @GetMapping("/api/v1/friends/search")
    public ApiResponse<MemberSearchResponse> searchFriends(
            @RequestParam("relation") boolean relation,
            @RequestParam("name") String name,
            SessionMember member){
        MemberSearchResponse response = memberService.searchFriend(member, relation, name);
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @PostMapping("/api/v1/friends")
    public ApiResponse<Boolean> addFriend(SessionMember member, @RequestBody MemberAddRequest request){
        Boolean result = memberService.addFriend(member, request);
        return ApiResponse.of(HttpStatus.OK,result);
    }
}
