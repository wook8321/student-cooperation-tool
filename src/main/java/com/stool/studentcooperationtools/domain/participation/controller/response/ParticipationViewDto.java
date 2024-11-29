package com.stool.studentcooperationtools.domain.participation.controller.response;

import com.stool.studentcooperationtools.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipationViewDto {

    private Long memberId;
    private String nickName;
    private String profile;

    @Builder
    private ParticipationViewDto(final Long memberId, final String nickName, final String profile) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.profile = profile;
    }

    public static ParticipationViewDto of(final Member member) {
        return ParticipationViewDto.builder()
                .memberId(member.getId())
                .profile(member.getProfile())
                .nickName(member.getNickName())
                .build();
    }
}
