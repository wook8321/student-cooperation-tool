package com.stool.studentcooperationtools.domain.member.controller.response;


import com.stool.studentcooperationtools.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberFindMemberDto {

    private String email;
    private String nickname;
    private String profile;

    @Builder
    private MemberFindMemberDto(final String email, final String nickname, final String profile) {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
    }

    public static MemberFindMemberDto of(Member member){
        return MemberFindMemberDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickName())
                .profile(member.getProfile())
                .build();
    }
}
