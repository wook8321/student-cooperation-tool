package com.stool.studentcooperationtools.domain.file.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadRequest {

    @NotNull
    private Long roomId;
    @NotNull
    private Long partId;
    @NotBlank
    private String fileName;
    @NotBlank
    private String fileCode;

    @Builder
    private FileUploadRequest(final Long roomId, final Long partId, final String fileName, final String fileCode) {
        this.roomId = roomId;
        this.partId = partId;
        this.fileName = fileName;
        this.fileCode = fileCode;
    }
}
