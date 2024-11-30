package com.stool.studentcooperationtools.websocket.controller.file.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileDeleteWebsocketRequest {

    @NotNull
    private Long roomId;
    @NotNull
    private Long partId;
    @NotNull
    private Long fileId;
    @NotBlank
    private String fileName;

    @Builder
    private FileDeleteWebsocketRequest(final Long roomId, final Long partId, final Long fileId, final String fileName) {
        this.roomId = roomId;
        this.partId = partId;
        this.fileId = fileId;
        this.fileName = fileName;
    }
}
