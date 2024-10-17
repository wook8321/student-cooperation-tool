package com.stool.studentcooperationtools.security.oauth2.dto;

import com.stool.studentcooperationtools.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionMember {

    private Long memberSeq;
    private String nickName;
    private String profile;

    @Builder
    private SessionMember(final Long memberSeq, final String nickName, final String profile) {
        this.memberSeq = memberSeq;
        this.nickName = nickName;
        this.profile = profile;
    }

    public static SessionMember of(final Member member) {
        return SessionMember.builder()
                .nickName(member.getNickName())
                .memberSeq(member.getId())
                .profile(member.getProfile())
                .build();
    }
}
