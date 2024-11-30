package com.stool.studentcooperationtools.websocket.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadWebsocketRequest {

    @NotNull
    private Long roomId;
    @NotNull
    private Long partId;
    @NotNull
    private Long fileId;
    @NotBlank
    private String fileType;
    @NotBlank
    private String fileName;
    @NotBlank
    private String originalName;

    @Builder
    private FileUploadWebsocketRequest(
            final Long roomId, final Long partId,
            final String fileName,final Long fileId,
            final String originalName,final String fileType
    ) {
        this.roomId = roomId;
        this.partId = partId;
        this.fileName = fileName;
        this.fileId = fileId;
        this.originalName = originalName;
        this.fileType = fileType;
    }
}
