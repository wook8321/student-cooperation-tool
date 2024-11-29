package com.stool.studentcooperationtools.websocket.controller.file.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileDeleteWebsocketResponse {

    private Long fileId;
    @Builder
    private FileDeleteWebsocketResponse(final Long fileId) {
        this.fileId = fileId;
    }

    public static FileDeleteWebsocketResponse of(final Long fileId){
        return FileDeleteWebsocketResponse.builder()
                .fileId(fileId)
                .build();
    }
}
