package com.stool.studentcooperationtools.domain.presentation.controller;

import com.google.auth.http.HttpCredentialsAdapter;
import com.stool.studentcooperationtools.domain.api.ApiResponse;
import com.stool.studentcooperationtools.domain.presentation.controller.response.PresentationFindResponse;
import com.stool.studentcooperationtools.domain.presentation.service.PresentationService;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
public class PresentationApiController {

    private final PresentationService presentationService;

    @GetMapping("/api/v1/rooms/{roomId}/presentation")
    public ApiResponse<PresentationFindResponse> findPresentation(@PathVariable("roomId") Long roomId){
        PresentationFindResponse response = presentationService.findPresentation(roomId);
        return ApiResponse.of(HttpStatus.OK,response);
    }

    @GetMapping("/api/v1/presentation/{presentationId}/exportPdf")
    public void exportToPdf(@PathVariable("presentationId") Long presentationId, HttpCredentialsAdapter credentialsAdapter,
                                                  HttpServletResponse response) throws IOException, GeneralSecurityException {
        ByteArrayOutputStream pdfStream = presentationService.exportPdf(credentialsAdapter, presentationId);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"exported.pdf\"");

        // ByteArrayOutputStream의 데이터를 응답으로 전송
        response.getOutputStream().write(pdfStream.toByteArray());
        response.getOutputStream().flush();
    }

    @GetMapping("/api/v1/presentation/{presentationId}/exportPptx")
    public void exportToPptx(Long presentationId, HttpCredentialsAdapter credentialsAdapter,
                                                   HttpServletResponse response) throws IOException, GeneralSecurityException {
        ByteArrayOutputStream pdfStream = presentationService.exportPpt(credentialsAdapter, presentationId);
        response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
        response.setHeader("Content-Disposition", "attachment; filename=\"exported.pptx\"");

        // ByteArrayOutputStream의 데이터를 응답으로 전송
        response.getOutputStream().write(pdfStream.toByteArray());
        response.getOutputStream().flush();
    }

}
