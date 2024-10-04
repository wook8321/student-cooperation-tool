package com.stool.studentcooperationtools.domain.member;

import com.stool.studentcooperationtools.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String profile;

    private Role role;

    @Builder
    private Member(final String nickName, final String profile, final Role role) {
        this.nickName = nickName;
        this.profile = profile;
        this.role = role;
    }

}
