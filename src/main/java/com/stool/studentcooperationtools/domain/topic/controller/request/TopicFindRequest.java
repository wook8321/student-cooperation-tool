package com.stool.studentcooperationtools.domain.topic.controller.request;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicFindRequest {

    @NotNull
    private Long roomId;

    @Builder
    private TopicFindRequest(final Long roomId) {
        this.roomId = roomId;
    }
}
