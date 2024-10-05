package com.stool.studentcooperationtools.websocket.controller.file;

import com.stool.studentcooperationtools.domain.file.service.FileService;
import com.stool.studentcooperationtools.s3.S3Service;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.WebsocketMessageType;
import com.stool.studentcooperationtools.websocket.WebsocketTestSupport;
import com.stool.studentcooperationtools.websocket.controller.file.request.FileDeleteWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.file.response.FileDeleteWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.file.response.FileUploadDto;
import com.stool.studentcooperationtools.websocket.controller.file.response.FileUploadWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.request.FileUploadWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.stool.studentcooperationtools.websocket.config.WebsocketConfig.PART_RESEARCH_URL_FORMAT;
import static org.assertj.core.api.Assertions.assertThat;

class FileWebsocketControllerTest extends WebsocketTestSupport {

    @MockBean
    private S3Service s3Service;
    @MockBean
    private FileService fileService;

    @DisplayName("웹소켓으로 파일을 업로드 요청을 받아서 업로드한 뒤, 결과값들을 유저들에게 전달한다.")
    @Test
    void fileUpload() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        String extension = "docs";
        String originalFileName = "파일.docs";
        String fileName = UUID.randomUUID().toString();
        long roomId = 1L;
        FileUploadWebsocketRequest request = FileUploadWebsocketRequest.builder()
                .fileCode("파일의 Base64 인코딩 코드")
                .fileName(originalFileName)
                .partId(1L)
                .roomId(roomId)
                .build();

        List<FileUploadDto> files = List.of(
                FileUploadDto.builder()
                        .fileId(1L)
                        .originalFileName(originalFileName)
                        .fileName(fileName)
                        .build()
        );

        FileUploadWebsocketResponse response = FileUploadWebsocketResponse.builder()
                .files(files)
                .num(files.size())
                .build();

        HashMap<String, List<String>> fileSet = new HashMap<>();
        fileSet.put(originalFileName,List.of(fileName,extension));

        Mockito.when(s3Service.uploadFile(Mockito.anyString(),Mockito.anyString()))
                        .thenReturn(fileSet);
        Mockito.when(fileService.addFile(
                Mockito.any(FileUploadWebsocketRequest.class),
                Mockito.any(HashMap.class),
                Mockito.any(SessionMember.class)
        )).thenReturn(response);

        stompSession.subscribe(PART_RESEARCH_URL_FORMAT.formatted(roomId),resultHandler);
        stompSession.send("/pub/file/upload",request);
        //when
        WebsocketResponse result = resultHandler.get(3);

        //then
        assertThat(result.getMessageType()).isEqualTo(WebsocketMessageType.PART_FILE_UPLOAD);
        assertThat(result.getData()).isNotNull();
    }

    @DisplayName("웹소켓으로 파일을 업로드 요청을 받아서 삭제한 뒤, 결과값들을 유저들에게 전달한다.")
    @Test
    void fileDelete() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        String fileName = UUID.randomUUID().toString();
        long roomId = 1L;
        long fileId = 1L;
        FileDeleteWebsocketRequest request = FileDeleteWebsocketRequest.builder()
                .fileId(fileId)
                .fileName(fileName)
                .roomId(roomId)
                .build();

        FileDeleteWebsocketResponse response = FileDeleteWebsocketResponse.builder()
                .fileId(fileId)
                .build();

        Mockito.when(fileService.deleteFile(
                Mockito.any(FileDeleteWebsocketRequest.class),
                Mockito.any(SessionMember.class)
        )).thenReturn(response);

        stompSession.subscribe(PART_RESEARCH_URL_FORMAT.formatted(roomId),resultHandler);
        stompSession.send("/pub/file/delete",request);
        //when
        WebsocketResponse result = resultHandler.get(3);

        //then
        assertThat(result.getMessageType()).isEqualTo(WebsocketMessageType.PART_FILE_REMOVE);
        assertThat(result.getData()).isNotNull();
    }

}