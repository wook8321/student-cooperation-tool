package com.stool.studentcooperationtools.domain.presentation;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
import com.stool.studentcooperationtools.domain.room.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Presentation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String presentationPath;

    @OneToOne(fetch = FetchType.LAZY)
    private Room room;

    @Builder
    private Presentation(final String presentationPath, final Room room) {
        this.presentationPath = presentationPath;
        this.room = room;
    }

    public void updatePath(String presentationPath) {
        this.presentationPath = presentationPath;
    }
}
