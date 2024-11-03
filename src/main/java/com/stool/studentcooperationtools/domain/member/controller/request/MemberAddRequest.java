package com.stool.studentcooperationtools.domain.member.controller.request;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAddRequest {

    @Email(message = "올바르지 않은 이메일 형식입니다.")
    private String email;

    @Builder
    private MemberAddRequest(final String email) {
        this.email = email;
    }
}
