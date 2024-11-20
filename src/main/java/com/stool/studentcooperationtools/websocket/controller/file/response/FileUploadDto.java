package com.stool.studentcooperationtools.websocket.controller.file.response;

import com.stool.studentcooperationtools.domain.file.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadDto {

    private Long fileId;
    private String originalFileName;
    private String fileName;

    @Builder
    private FileUploadDto(final Long fileId, final String originalFileName, final String fileName) {
        this.fileId = fileId;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
    }

    public static FileUploadDto of(final File file){
        return FileUploadDto.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .originalFileName(file.getOriginalName())
                .build();
    }

}
