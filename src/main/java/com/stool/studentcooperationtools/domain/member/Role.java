package com.stool.studentcooperationtools.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    USER("ROLE_USER","유저"),
    ADMIN("ROLE_ADMIN","관리자");

    private String key;
    private String title;
}
