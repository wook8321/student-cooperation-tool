package com.stool.studentcooperationtools.websocket.controller.part.response;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.part.Part;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartAddWebsocketResponse {

    private Long partId;
    private Long memberId;
    private String nickName;
    private String profile;
    private LocalDate createTime;
    private String partName;

    @Builder
    private PartAddWebsocketResponse(final Long memberId,final Long partId, final String nickName, final String profile, final LocalDate createTime, final String partName) {
        this.memberId = memberId;
        this.partId = partId;
        this.profile = profile;
        this.nickName = nickName;
        this.createTime = createTime;
        this.partName = partName;
    }

    public static PartAddWebsocketResponse of(final Part part,final Member member) {
        return PartAddWebsocketResponse.builder()
                .partId(part.getId())
                .memberId(part.getMember().getId())
                .nickName(member.getNickName())
                .profile(member.getProfile())
                .createTime(part.getCreatedTime())
                .partName(part.getPartName())
                .build();
    }
}
