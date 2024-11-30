package com.stool.studentcooperationtools.domain.file.controller.response;

import com.stool.studentcooperationtools.domain.file.File;
import com.stool.studentcooperationtools.domain.file.FileType;
import com.stool.studentcooperationtools.s3.S3Service;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PartFindFileDto {

    private Long fileId;
    private String fileName;
    private FileType fileType;
    private String originalName;
    private String fileUrl;

    @Builder
    private PartFindFileDto(
            final String fileName, final FileType fileType,
            final String originalName, final Long fileId, final String fileUrl
    ) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.originalName = originalName;
        this.fileUrl = fileUrl;
    }

    public static PartFindFileDto of(File file) {
        return PartFindFileDto.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .fileUrl(S3Service.getS3FileUrl(file.getFileName()))
                .originalName(file.getOriginalName())
                .build();
    }
}
