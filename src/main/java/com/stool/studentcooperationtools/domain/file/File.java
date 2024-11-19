package com.stool.studentcooperationtools.domain.file;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
import com.stool.studentcooperationtools.domain.part.Part;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(nullable = false)
    private String originalName;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Part part;

    @Builder
    private File(
            final String fileName,
            final FileType fileType,
            final String originalName,
            final Part part
    ) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.originalName = originalName;
        this.part = part;
    }

    public static File of(
            final String fileName,
            final FileType fileType,
            final String originalName,
            final Part part){
        File file = File.builder()
                .fileName(fileName)
                .fileType(fileType)
                .originalName(originalName)
                .part(part)
                .build();

        part.addFile(file);
        return file;
    }
}
