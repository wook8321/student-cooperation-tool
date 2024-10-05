package com.stool.studentcooperationtools.domain.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum FileType {

    PDF("pdf"),
    JPEG("image"),
    DOCS("docs"),
    PNG("png"),
    JPG("jpg");

    private String key;

    public static FileType getFileType(final String extension){
        return Arrays.stream(FileType.values())
                .filter(fileType -> fileType.getKey().equals(extension))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 파일은 지원하지 않습니다."));
    }
}
