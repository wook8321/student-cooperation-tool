package com.stool.studentcooperationtools.websocket.controller.part.response;

import com.stool.studentcooperationtools.domain.part.Part;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartUpdateWebsocketResponse {

    private Long partId;
    private Long memberId;
    private String partName;
    private String nickName;
    private String profile;

    @Builder
    private PartUpdateWebsocketResponse(final Long partId, final Long memberId, final String partName, final String nickName, final String profile) {
        this.partId = partId;
        this.memberId = memberId;
        this.partName = partName;
        this.nickName = nickName;
        this.profile = profile;
    }

    public static PartUpdateWebsocketResponse of(final Part part){
        return PartUpdateWebsocketResponse.builder()
                .nickName(part.getMember().getNickName())
                .partId(part.getId())
                .memberId(part.getMember().getId())
                .profile(part.getMember().getProfile())
                .partName(part.getPartName())
                .build();
    }
}
