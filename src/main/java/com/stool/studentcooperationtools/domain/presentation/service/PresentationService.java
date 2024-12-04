package com.stool.studentcooperationtools.domain.presentation.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.slides.v1.Slides;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.common.base.VerifyException;
import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.presentation.controller.response.PresentationFindResponse;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.slide.SlidesFactory;
import com.stool.studentcooperationtools.domain.slide.service.SlideService;
import com.stool.studentcooperationtools.security.credential.GoogleCredentialProvider;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.presentation.request.PresentationCreateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.presentation.request.PresentationUpdateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.presentation.response.PresentationUpdateSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final RoomRepository roomRepository;
    private final SlidesFactory slidesFactory;
    private final GoogleCredentialProvider googleCredentialProvider;
    private final SlideService slideService;

    public PresentationFindResponse findPresentation(final Long roomId) {
        Presentation presentation = presentationRepository.findByRoomId(roomId)
                .orElseThrow(()-> new IllegalArgumentException("해당 방의 발표자료가 존재하지 않습니다"));
        return PresentationFindResponse.builder()
                .presentationId(presentation.getId())
                .presentationPath(presentation.getPresentationPath())
                .build();
    }

    @Transactional
    public PresentationUpdateSocketResponse updatePresentation(final PresentationUpdateSocketRequest request, SessionMember member) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(()->new IllegalArgumentException("해당 방은 존재하지 않습니다"));
        Credential credential = googleCredentialProvider.getCredential();
        if(!room.getLeader().getId().equals(member.getMemberSeq())){
            throw new IllegalArgumentException("발표자료 변경 권한이 없습니다");
        }
        Presentation updatingPpt = presentationRepository.findByRoomId(room.getId())
                .orElseGet(()->{
                    Presentation newPpt = Presentation.builder()
                            .room(room)
                            .presentationPath(request.getPresentationPath())
                            .build();
                    presentationRepository.save(newPpt);
                    return newPpt;
                });
        updatingPpt.updatePath(request.getPresentationPath());
        slideService.updateSlides(updatingPpt.getId(),credential);
        return PresentationUpdateSocketResponse.of(updatingPpt);
    }

    @Transactional
    public PresentationUpdateSocketResponse createPresentation(PresentationCreateSocketRequest request,
                                                               Credential credential,
                                                               SessionMember member) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(()->new IllegalArgumentException("해당 방은 존재하지 않습니다"));
        if(!room.getLeader().getId().equals(member.getMemberSeq())){
            throw new IllegalArgumentException("발표자료 변경 권한이 없습니다");
        }
        String fileId;
        Drive dservice = slidesFactory.createDriveServicePerUser(credential);
        File fileMetadata = new File();
        fileMetadata.setName(request.getPresentationName());
        fileMetadata.setMimeType("application/vnd.google-apps.presentation");
        // Drive에서 프레젠테이션 파일을 해당 폴더에 저장
        try {
            File file = dservice.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            fileId = file.getId();  // pptPath 반환
            Permission permission = new Permission()
                    .setType("anyone")
                    .setRole("writer");
            dservice.permissions().create(fileId, permission).execute();
        }
        catch (IOException e) {
            throw new VerifyException(e.getMessage(),e.getCause());
        }
        if(presentationRepository.existsByRoomId(room.getId())){
            Presentation presentation = presentationRepository.findByRoomId(room.getId())
                    .orElseThrow(() -> new IllegalArgumentException("발표자료를 들고 오던 중 에러 발생"));
            presentation.updatePath(fileId);
            slideService.updateSlides(presentation.getId(),credential);
            return PresentationUpdateSocketResponse.of(presentation);
        }
        else {
            Presentation presentation = Presentation.builder()
                    .room(room)
                    .presentationPath(fileId)
                    .build();
            presentationRepository.save(presentation);
            slideService.updateSlides(presentation.getId(),credential);
            return PresentationUpdateSocketResponse.of(presentation);
        }
    }

    public ByteArrayOutputStream exportPdf(Credential credential, Long presentationId) {
        Presentation presentation = presentationRepository.findById(presentationId)
                .orElseThrow(()->new IllegalArgumentException("해당하는 발표자료가 없습니다"));
        String fileId = presentation.getPresentationPath();
        Drive driveService = slidesFactory.createDriveServicePerUser(credential);
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            driveService.files().export(fileId, "application/pdf")
                    .executeMediaAndDownloadTo(outputStream);

            return (ByteArrayOutputStream) outputStream;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }

    public ByteArrayOutputStream exportPpt(Credential credential, Long presentationId) {
        Presentation presentation = presentationRepository.findById(presentationId)
                .orElseThrow(()->new IllegalArgumentException("해당하는 발표자료가 없습니다"));
        String fileId = presentation.getPresentationPath();
        Drive driveService = slidesFactory.createDriveServicePerUser(credential);
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            driveService.files().export(fileId, "application/vnd.openxmlformats-officedocument.presentationml.presentation")
                    .executeMediaAndDownloadTo(outputStream);

            return (ByteArrayOutputStream) outputStream;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }
    public Boolean deletePresentation(Long roomId) {
        try {
            googleCredentialProvider.initializeCredentialAdapter();
            HttpCredentialsAdapter credentialsAdapter = googleCredentialProvider.getCredentialsAdapter();
            Presentation presentation = presentationRepository.findByRoomId(roomId)
                    .orElse(null);
            if (presentation == null) {
                return true;
            }
            String fileId = presentation.getPresentationPath();
            Drive driveService = slidesFactory.createDriveService(credentialsAdapter);
            driveService.files().delete(fileId).execute();
            return true;
        }
        catch(IOException e){
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }

    public Boolean checkValidPPT(Credential credential, String presentationPath) {
        Slides service = slidesFactory.createSlidesService(credential);
        try {
            service.presentations().get(presentationPath).execute();
        } catch(IOException | IllegalStateException e){
            throw new IllegalArgumentException(e.getMessage(),e.getCause());
        }
        return true;
    }
}
