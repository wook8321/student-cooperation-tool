package com.stool.studentcooperationtools.websocket.controller.file.response;

import com.stool.studentcooperationtools.domain.file.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadWebsocketResponse {

    private int num;
    private List<FileUploadDto> files;

    @Builder
    public FileUploadWebsocketResponse(final int num, final List<FileUploadDto> files) {
        this.num = num;
        this.files = files;
    }

    public static FileUploadWebsocketResponse of(final List<File> files){
        return FileUploadWebsocketResponse.builder()
                .num(files.size())
                .files(files.stream()
                        .map(FileUploadDto::of).toList()
                )
                .build();
    }
}
