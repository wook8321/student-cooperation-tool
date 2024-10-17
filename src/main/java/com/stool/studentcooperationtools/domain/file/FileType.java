package com.stool.studentcooperationtools.domain.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    PDF("pdf"),
    IMAGE("image"),
    TEXT("text");

    private String key;

}
