package com.stool.studentcooperationtools.websocket.controller.enterRoon.domain;

import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Online {

    private Long memberId;
    private String profile;
    private String nickName;

    @Builder
    private Online(final Long memberId, final String profile, final String nickName) {
        this.memberId = memberId;
        this.profile = profile;
        this.nickName = nickName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Online online = (Online) o;
        return Objects.equals(memberId, online.memberId) && Objects.equals(profile, online.profile) && Objects.equals(nickName, online.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, profile, nickName);
    }

    public static Online of(final SessionMember sessionMember) {
        return Online.builder()
                .memberId(sessionMember.getMemberSeq())
                .profile(sessionMember.getProfile())
                .nickName(sessionMember.getNickName())
                .build();
    }
}
