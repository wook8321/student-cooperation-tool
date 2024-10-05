package com.stool.studentcooperationtools.domain.api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiExceptionResponse<T> {

    private HttpStatus status;
    private int code;
    private String message;
    private T data;

    @Builder
    private ApiExceptionResponse(final HttpStatus status, final int code, final String message, final T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T>ApiExceptionResponse<T> of(final HttpStatus status, final String message, final T data){
        return new ApiExceptionResponse<>(status,status.value(),message,data);
    }
}
