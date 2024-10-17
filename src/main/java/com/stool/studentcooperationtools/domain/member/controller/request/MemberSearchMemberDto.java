package com.stool.studentcooperationtools.domain.member.controller.request;

import com.stool.studentcooperationtools.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSearchMemberDto {

    private String email;
    private String nickname;
    private String profile;

    @Builder
    private MemberSearchMemberDto(final String email, final String nickname, final String profile) {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
    }

    public static MemberSearchMemberDto of(Member member){
        return MemberSearchMemberDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickName())
                .profile(member.getProfile())
                .build();
    }

}
