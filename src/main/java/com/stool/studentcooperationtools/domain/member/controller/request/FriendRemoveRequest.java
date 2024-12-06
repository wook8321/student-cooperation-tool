package com.stool.studentcooperationtools.domain.member.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendRemoveRequest {

    private long memberId;

    @Builder
    private FriendRemoveRequest(final long memberId) {
        this.memberId = memberId;
    }

}
