package com.stool.studentcooperationtools.domain.participation.controller.response;

import com.stool.studentcooperationtools.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipationViewResponse {

    private int num;
    private List<ParticipationViewDto> participations;

    @Builder
    public ParticipationViewResponse(final int num, final List<ParticipationViewDto> participations) {
        this.num = num;
        this.participations = participations;
    }

    public static ParticipationViewResponse of(final List<Member> members) {
        return ParticipationViewResponse.builder()
                .num(members.size())
                .participations(
                        members.stream().map(ParticipationViewDto::of)
                        .toList()
                )
                .build();
    }
}
