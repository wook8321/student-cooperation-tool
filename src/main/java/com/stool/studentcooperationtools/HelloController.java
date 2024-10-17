package com.stool.studentcooperationtools;

import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/api/test")
    public String hello() {
        return "hello";
    }

    @GetMapping("/api/loginTest")
    public ResponseEntity<SessionMember> loginTest(SessionMember member){
        return new ResponseEntity<>(member, HttpStatus.OK);
    }
}