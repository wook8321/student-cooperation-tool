package com.stool.studentcooperationtools.domain.file.controller;

import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.room.service.RoomService;
import com.stool.studentcooperationtools.s3.S3Service;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileApiController {

    private final S3Service s3Service;
    private final RoomService roomService;

    //방의 자료를 다운로드 요청을 다루는 핸들러이다.
    //해당방의 자원을 다운로드 받을 권한이 없다면 AccessDenied 예외가 발생한다.
    //Response Header에 CONTENT_DISPOSITION과 body에는 다운 받을 자원인 UrlResource를 넣는다.
    @GetMapping("/api/v1/files/{fileName}")
    public ApiResponse<UrlResource> downloadFile(
            @PathVariable("fileName") String fileName,
            @RequestParam("roomId") Long roomId,
            @RequestParam("fileOriginalName") String originalFileName,
            SessionMember sessionMember,
            HttpServletResponse response
    ){
        //해당 방의 참여자가 아니면 다운로드를 거부한다.
        roomService.validParticipationInRoom(roomId,sessionMember);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, s3Service.getContentDisposition(originalFileName));
        return ApiResponse.of(HttpStatus.OK,s3Service.getUrlResource(fileName));
    }

}
