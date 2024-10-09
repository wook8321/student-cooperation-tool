package com.stool.studentcooperationtools.domain.member.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.member.controller.request.MemberAddRequest;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberFindResponse;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberSearchResponse;
import com.stool.studentcooperationtools.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/friends")
    public ApiResponse<MemberFindResponse> findFriends(){
        MemberFindResponse response = memberService.findFriends();
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @GetMapping("/api/v1/friends/search")
    public ApiResponse<MemberSearchResponse> searchFriends(
            @RequestParam("relation") boolean relation,
            @RequestParam("name") String name
                                                           ){
        MemberSearchResponse response = memberService.searchFriend(relation, name);
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @PostMapping("/api/v1/friends")
    public ApiResponse<Boolean> addFriend(@RequestBody MemberAddRequest request){
        Boolean result = memberService.addFriend(request);
        return ApiResponse.of(HttpStatus.OK,result);
    }
}
