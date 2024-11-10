package com.stool.studentcooperationtools.domain.presentation.service;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.slides.v1.Slides;
import com.google.auth.http.HttpCredentialsAdapter;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.slide.SlidesFactory;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.presentation.request.PresentationCreateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.presentation.response.PresentationUpdateSocketResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresentationServiceMockTest {

    @InjectMocks
    PresentationService presentationService;

    @Mock
    SlidesFactory slidesFactory;

    @Mock
    Drive driveService;

    @Mock
    HttpCredentialsAdapter credentialsAdapter;

    @Mock
    RoomRepository roomRepository;

    @Mock
    PresentationRepository presentationRepository;

    @Mock
    Drive.Files files;

    @Mock
    Drive.Files.Create filesCreate;

    @Mock
    Drive.Permissions permissions;

    @Mock
    Drive.Permissions.Create permissionsCreate;

    @Mock
    Drive.Permissions.List permissionsList;

    @Mock
    PermissionList permissionList;

    @Mock
    List<Permission> listOfPermission;

    @Mock
    Room room;

    @Mock
    SessionMember member;

    @Test
    @DisplayName("발표자료를 설정한 제목으로 생성")
    void createPresentation() throws GeneralSecurityException, IOException {
        //given
        Long memberId = 1L;
        listOfPermission = new ArrayList<>();
        File file = new File()
                .setName("new")
                .setId("abc")
                .setMimeType("application/vnd.google-apps.presentation");
        PresentationCreateSocketRequest request = PresentationCreateSocketRequest.builder()
                .presentationName("new")
                .roomId(1L)
                .build();
        when(slidesFactory.createDriveService(credentialsAdapter)).thenReturn(driveService);
        when(driveService.files()).thenReturn(files);
        when(files.create(any(File.class))).thenReturn(filesCreate);
        when(filesCreate.setFields(anyString())).thenReturn(filesCreate);
        when(filesCreate.execute()).thenReturn(file);
        when(driveService.permissions()).thenReturn(permissions);
        when(permissions.create(anyString(), any(Permission.class))).thenReturn(permissionsCreate);
        when(permissionsCreate.execute()).thenReturn(mock(Permission.class));
        when(permissions.list(anyString())).thenReturn(permissionsList);
        when(permissionsList.execute()).thenReturn(permissionList);
        when(permissionList.getPermissions()).thenReturn(listOfPermission);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(room.getLeader()).thenReturn(mock(Member.class));
        when(room.getLeader().getId()).thenReturn(memberId);
        when(member.getMemberSeq()).thenReturn(memberId);
        //when
        PresentationUpdateSocketResponse response = presentationService.createPresentation(request,
                credentialsAdapter, member);
        //then
        assertNotNull(response);
        assertEquals(response.getPresentationPath(), "abc");
    }
}