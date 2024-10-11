package com.stool.studentcooperationtools.security.oauth2.dto;

import com.stool.studentcooperationtools.domain.member.Member;
import lombok.Getter;

@Getter
public class SessionMember {

    private Long memberSeq;
    private String nickName;
    private String profile;

    public SessionMember(final Member member) {
        this.memberSeq = member.getId();
        this.nickName = member.getNickName();
        this.profile = member.getProfile();
    }
}
