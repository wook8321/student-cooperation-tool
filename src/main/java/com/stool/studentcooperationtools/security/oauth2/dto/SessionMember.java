package com.stool.studentcooperationtools.security.oauth2.dto;

import com.stool.studentcooperationtools.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionMember implements Serializable {

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
