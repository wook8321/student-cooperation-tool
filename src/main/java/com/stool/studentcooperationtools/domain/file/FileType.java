package com.stool.studentcooperationtools.domain.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum FileType {

    PDF("pdf",
            "data:application/pdf;base64",
            "application/pdf"
    ),
    XLS("xls",
            "data:application/vnd.ms-excel;base64",
            "image/png"
    ),
    XLSX("xlsx",
            "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    ),
    DOCX("docx",
            "data:application/vnd.openxmlformats-officedocument.wordprocessingml.document;base64",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ),
    PNG("png",
            "data:image/png;base64",
            "image/png"
    ),
    JPG("jpg",
            "data:image/jpeg;base64",
            "image/jpg"
    ),
    JPEG(
            "jpeg",
            "data:image/jpg;base64",
            "image/jpeg"
    );

    private String key;
    private String base64;
    private String mimeType;
    private static final  Map<String,String> base64Map = createFileMap();
    private static final Map<String,FileType> fileTypeMap = createfileTypeMap();
    private static final Map<String, String> mimeTypeMap = createMimeType();

    private static Map<String, String> createMimeType(){
        return new HashMap<>(
                Arrays.stream(FileType.values())
                        .collect(Collectors.toMap(FileType::getKey,FileType::getMimeType))
        );
    }

    private static Map<String, String> createFileMap(){
        return new HashMap<>(
                Arrays.stream(FileType.values())
                        .collect(Collectors.toMap(FileType::getBase64,FileType::getKey))
        );
    }

    private static Map<String, FileType> createfileTypeMap(){
        return new HashMap<>(
                Arrays.stream(FileType.values())
                        .collect(Collectors.toMap(FileType::getKey, Function.identity()))
        );
    }

    public static String getMimeType(final String extension){
        return Optional.ofNullable(mimeTypeMap.get(extension))
                .orElseThrow(() -> new IllegalArgumentException("해당 파일은 지원하지 않습니다."));
    }

    public static FileType getFileType(final String extension){
        return Optional.ofNullable(fileTypeMap.get(extension))
                .orElseThrow(() -> new IllegalArgumentException("해당 파일은 지원하지 않습니다."));
    }

    public static String getFileExtension(final String base64Encoding){
        return Optional.ofNullable(base64Map.get(base64Encoding))
                .orElseThrow(() -> new IllegalArgumentException("해당 파일은 지원하지 않습니다."));
    }
}
