package com.stool.studentcooperationtools.websocket.controller.file.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileDeleteWebsocketResponse {

    private Long fileId;
    private Long partId;

    @Builder
    private FileDeleteWebsocketResponse(final Long fileId, final Long partId) {
        this.fileId = fileId;
        this.partId = partId;
    }

    public static FileDeleteWebsocketResponse of(final Long fileId, final Long partId){
        return FileDeleteWebsocketResponse.builder()
                .fileId(fileId)
                .partId(partId)
                .build();
    }
}
