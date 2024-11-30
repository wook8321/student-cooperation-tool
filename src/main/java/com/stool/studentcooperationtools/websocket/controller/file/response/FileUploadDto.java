package com.stool.studentcooperationtools.websocket.controller.file.response;

import com.stool.studentcooperationtools.domain.file.File;
import com.stool.studentcooperationtools.domain.file.FileType;
import com.stool.studentcooperationtools.s3.S3Service;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadDto {

    private Long fileId;
    private String originalName;
    private FileType fileType;
    private String fileName;
    private String fileUrl;

    @Builder
    private FileUploadDto(final Long fileId, final String originalName,final FileType fileType, final String fileName,final String fileUrl) {
        this.fileId = fileId;
        this.originalName = originalName;
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public static FileUploadDto of(final File file){
        return FileUploadDto.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .originalName(file.getOriginalName())
                .fileUrl(S3Service.getS3FileUrl(file.getFileName()))
                .build();
    }


}
