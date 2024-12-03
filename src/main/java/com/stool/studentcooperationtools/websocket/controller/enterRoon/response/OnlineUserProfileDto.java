package com.stool.studentcooperationtools.websocket.controller.enterRoon.response;

import com.stool.studentcooperationtools.websocket.controller.enterRoon.domain.Online;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnlineUserProfileDto {

    private Long memberId;
    private String profile;
    private String nickName;

    @Builder
    private OnlineUserProfileDto(final Long memberId, final String profile, final String nickName) {
        this.memberId = memberId;
        this.profile = profile;
        this.nickName = nickName;
    }

    public static OnlineUserProfileDto of(final Online online){
        return OnlineUserProfileDto.builder()
                .memberId(online.getMemberId())
                .profile(online.getProfile())
                .nickName(online.getNickName())
                .build();
    }
}
