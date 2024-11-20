package com.stool.studentcooperationtools;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ReactController {

    @GetMapping(value = {"/{x:^(?!api$|oauth$|static).*}"})
    public ResponseEntity<Object> serveReactApp(@PathVariable String x) {
        try {
            ClassPathResource indexHtml = new ClassPathResource("static/index.html");
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "text/html")
                    .body(indexHtml.getInputStream().readAllBytes());
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to load React app. cause = " + e.getMessage());
        }
    }
}
