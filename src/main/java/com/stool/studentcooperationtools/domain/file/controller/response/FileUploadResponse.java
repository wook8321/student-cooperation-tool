package com.stool.studentcooperationtools.domain.file.controller.response;

import com.stool.studentcooperationtools.domain.file.File;
import com.stool.studentcooperationtools.websocket.controller.file.response.FileUploadDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadResponse {
    private int num;
    private List<FileUploadDto> files;

    @Builder
    public FileUploadResponse(final int num, final List<FileUploadDto> files) {
        this.num = num;
        this.files = files;
    }

    public static FileUploadResponse of(final List<File> files){
        return FileUploadResponse.builder()
                .num(files.size())
                .files(files.stream()
                        .map(FileUploadDto::of).toList()
                )
                .build();
    }
}
