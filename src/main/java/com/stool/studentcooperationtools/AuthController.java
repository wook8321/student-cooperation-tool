package com.stool.studentcooperationtools;

import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/auth/check")
    public ResponseEntity<?> checkAuth(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @GetMapping("/user-info")
    public ResponseEntity<Long> userInfo(SessionMember sessionMember){
        return new ResponseEntity<>(sessionMember.getMemberSeq(), HttpStatus.OK);
    }
}