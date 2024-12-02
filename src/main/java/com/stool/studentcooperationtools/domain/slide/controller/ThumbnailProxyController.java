package com.stool.studentcooperationtools.domain.slide.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/proxy")
public class ThumbnailProxyController {

    @GetMapping("/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(@RequestParam String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Google 썸네일 URL 요청
            byte[] imageBytes = restTemplate.getForObject(URI.create(url), byte[].class);

            // Content-Type 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/jpeg");
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }
}