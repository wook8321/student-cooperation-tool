package com.stool.studentcooperationtools.websocket.controller.file;

import com.stool.studentcooperationtools.domain.file.service.FileService;
import com.stool.studentcooperationtools.s3.S3Service;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.Utils.SimpleMessageSendingUtils;
import com.stool.studentcooperationtools.websocket.controller.file.response.FileDeleteWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.file.request.FileDeleteWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.file.response.FileUploadWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.request.FileUploadWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.request.WebsocketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;

import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.PART_FILE_REMOVE;
import static com.stool.studentcooperationtools.websocket.WebsocketMessageType.PART_FILE_UPLOAD;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileWebsocketController {

    private final S3Service s3Service;
    private final SimpleMessageSendingUtils sendingUtils;
    private final FileService fileService;

    @MessageMapping("/file/upload")
    public void fileUpload(@Valid @RequestBody FileUploadWebsocketRequest request, SessionMember sessionMember){
        HashMap<String, List<String>> fileSet = s3Service.uploadFile(request.getFileName(), request.getFileCode());
        FileUploadWebsocketResponse response = fileService.addFile(request,fileSet,sessionMember);
        sendingUtils.convertAndSend(
                sendingUtils.creatPartResearchSubUrl(request.getRoomId()),
                WebsocketResponse.of(PART_FILE_UPLOAD,response)
        );
    }

    @MessageMapping("/file/delete")
    public void fileDelete(@Valid @RequestBody FileDeleteWebsocketRequest request, SessionMember sessionMember){
        FileDeleteWebsocketResponse response = fileService.deleteFile(request,sessionMember);
        sendingUtils.convertAndSend(
                sendingUtils.creatPartResearchSubUrl(request.getRoomId()),
                WebsocketResponse.of(PART_FILE_REMOVE,response)
        );
    }
}
