package com.stool.studentcooperationtools.websocket.controller.file.response;

import com.stool.studentcooperationtools.s3.S3Service;
import com.stool.studentcooperationtools.websocket.controller.request.FileUploadWebsocketRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadWebsocketResponse {

    private Long roomId;
    private Long partId;
    private Long fileId;
    private String fileType;
    private String fileName;
    private String fileUrl;
    private String originalName;

    @Builder
    private FileUploadWebsocketResponse(final Long roomId, final Long partId, final Long fileId, final String fileName, final String fileType, final String fileUrl, final String originalName) {
        this.roomId = roomId;
        this.partId = partId;
        this.fileId = fileId;
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.originalName = originalName;
    }

    public static FileUploadWebsocketResponse of(final FileUploadWebsocketRequest request) {
        return FileUploadWebsocketResponse.builder()
                .roomId(request.getRoomId())
                .partId(request.getPartId())
                .fileId(request.getFileId())
                .fileType(request.getFileType())
                .fileName(request.getFileName())
                .fileUrl(S3Service.getS3FileUrl(request.getFileName()))
                .originalName(request.getOriginalName())
                .build();
    }
}
