package com.stool.studentcooperationtools.domain.member.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.member.controller.request.MemberAddRequest;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberFindResponse;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberSearchResponse;
import com.stool.studentcooperationtools.domain.member.service.MemberService;
<<<<<<< HEAD
=======
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/friends")
<<<<<<< HEAD
    public ApiResponse<MemberFindResponse> findFriends(){
        MemberFindResponse response = memberService.findFriends();
=======
    public ApiResponse<MemberFindResponse> findFriends(SessionMember member){
        MemberFindResponse response = memberService.findFriends(member);
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @GetMapping("/api/v1/friends/search")
    public ApiResponse<MemberSearchResponse> searchFriends(
            @RequestParam("relation") boolean relation,
<<<<<<< HEAD
            @RequestParam("name") String name
                                                           ){
        MemberSearchResponse response = memberService.searchFriend(relation, name);
=======
            @RequestParam("name") String name,
            SessionMember member){
        MemberSearchResponse response = memberService.searchFriend(member, relation, name);
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @PostMapping("/api/v1/friends")
<<<<<<< HEAD
    public ApiResponse<Boolean> addFriend(@RequestBody MemberAddRequest request){
        Boolean result = memberService.addFriend(request);
=======
    public ApiResponse<Boolean> addFriend(SessionMember member, @RequestBody MemberAddRequest request){
        Boolean result = memberService.addFriend(member, request);
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
        return ApiResponse.of(HttpStatus.OK,result);
    }
}
