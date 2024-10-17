package com.stool.studentcooperationtools.domain.member.controller.request;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAddRequest {

<<<<<<< HEAD
    @Email(message = "옳바르지 않는 이메일 형식입니다.")
=======
    @Email(message = "올바르지 않은 이메일 형식입니다.")
>>>>>>> daa7c781c6c5fad1d18527e8a96ea8b1d0f2a862
    private String email;

    @Builder
    private MemberAddRequest(final String email) {
        this.email = email;
    }
}
