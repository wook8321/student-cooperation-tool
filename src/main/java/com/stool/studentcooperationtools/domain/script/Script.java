package com.stool.studentcooperationtools.domain.script;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
import com.stool.studentcooperationtools.domain.presentation.Presentation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Script extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    private Presentation presentation;

    @Column(nullable = false)
    private String script;

    @Builder
    private Script(
            final String thumbnail, final Presentation presentation,
            final String script) {
        this.thumbnail = thumbnail;
        this.presentation = presentation;
        this.script = script;
    }

}
