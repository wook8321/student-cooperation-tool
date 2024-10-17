package com.stool.studentcooperationtools.domain.file.controller.response;

import com.stool.studentcooperationtools.domain.file.File;
import com.stool.studentcooperationtools.domain.file.FileType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PartFindFileDto {

    private String fileName;
    private FileType fileType;
    private String filePath;
    private String originalName;

    @Builder
    private PartFindFileDto(
            final String fileName, final FileType fileType,
            final String filePath, final String originalName
    ) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.originalName = originalName;
    }

    public static PartFindFileDto of(File file) {
        return PartFindFileDto.builder()
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .filePath(file.getFilePath())
                .originalName(file.getOriginalName())
                .build();
    }
}
