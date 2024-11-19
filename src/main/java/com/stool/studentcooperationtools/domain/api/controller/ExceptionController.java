package com.stool.studentcooperationtools.domain.api.controller;


import com.stool.studentcooperationtools.domain.api.ApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;


@RestControllerAdvice
public class ExceptionController {

    //요청값을 바인딩할 때 발생하는 예외를 처리한다.
    //@Valid가 붙은 파라미터를 바인딩할 때 발생하는 예외이다.(ex @NotBlank로 설정한 값이 빈칸,null일 경우)
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse<Object> bindException(BindException exception){
        return ApiExceptionResponse.of(
            HttpStatus.BAD_REQUEST,
                exception.getBindingResult()
                        .getAllErrors().get(0)
                        .getDefaultMessage(),
                null
        );
    }

    //클라이언트에서 받은 값을 사용할 때 유효하지 않은 값일 경우 발생하는 예외를 처리한다.
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse<Object> IllegalArgumentException(IllegalArgumentException exception){
        return ApiExceptionResponse.of(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                null
        );
    }

    //권한이 없는 작업을 클라이언트에서 요청할 때, 발생하는 예외를 처리한다.
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.BAD_GATEWAY)
    public ApiExceptionResponse<Object> AccessDeniedException(AccessDeniedException exception){
        return ApiExceptionResponse.of(
                HttpStatus.BAD_GATEWAY,
                exception.getMessage(),
                null
        );
    }

}
