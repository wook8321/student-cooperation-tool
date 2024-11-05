package com.stool.studentcooperationtools.websocket.controller.part.response;

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
    private LocalDate createTime;
    private String partName;

    @Builder
    private PartAddWebsocketResponse(final Long partId, final LocalDate createTime, final String partName) {
        this.partId = partId;
        this.createTime = createTime;
        this.partName = partName;
    }

    public static PartAddWebsocketResponse of(final Part part) {
        return PartAddWebsocketResponse.builder()
                .partId(part.getId())
                .createTime(part.getCreatedTime())
                .partName(part.getPartName())
                .build();
    }
}
