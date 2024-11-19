package com.stool.studentcooperationtools.domain.room.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.participation.Participation;
import com.stool.studentcooperationtools.domain.participation.repository.ParticipationRepository;
import com.stool.studentcooperationtools.domain.presentation.service.PresentationService;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomRemoveRequest;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RoomServiceMockTest {

    @MockBean
    private PresentationService presentationService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        participationRepository.deleteAll();
        roomRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("팀원이 방 삭제 요청 시 방 참여 인원에서 삭제")
    void removeRoomByTeamMate() throws GeneralSecurityException, IOException {
        // Given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);

        Member leader = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("leader")
                .build();
        memberRepository.save(leader);

        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(leader)
                .password("password")
                .build();
        roomRepository.save(room);

        participationRepository.save(Participation.of(user, room));
        participationRepository.save(Participation.of(leader, room));

        RoomRemoveRequest roomRemoveRequest = RoomRemoveRequest.builder()
                .roomId(room.getId())
                .build();

        when(presentationService.deletePresentation(anyLong())).thenReturn(true);

        // When
        roomService.removeRoom(member, roomRemoveRequest);

        // Then
        assertThat(roomRepository.existsById(room.getId())).isTrue();
        assertThat(participationRepository.existsByMemberIdAndRoomId(user.getId(), room.getId())).isFalse();
    }

    @Test
    @DisplayName("방장이 방 삭제 요청 시 방과 모든 참여 인원이 삭제")
    void removeRoomByLeader() throws GeneralSecurityException, IOException {
        //given
        Member leader = Member.builder()
                .role(Role.USER)
                .email("leaderEmail")
                .profile("leaderProfile")
                .nickName("leaderNick")
                .build();
        memberRepository.save(leader);

        Room room = Room.builder()
                .title("roomTitle")
                .participationNum(1)
                .leader(leader)
                .password("roomPassword")
                .build();
        roomRepository.save(room);

        participationRepository.save(Participation.of(leader, room));

        RoomRemoveRequest roomRemoveRequest = RoomRemoveRequest.builder()
                .roomId(room.getId())
                .build();
        SessionMember sessionMember = SessionMember.of(leader);

        when(presentationService.deletePresentation(anyLong())).thenReturn(true);

        //when
        Boolean result = roomService.removeRoom(sessionMember, roomRemoveRequest);

        //then
        assertTrue(result);
        assertThat(roomRepository.existsById(room.getId())).isFalse();
        assertThat(participationRepository.existsByRoomId(room.getId())).isFalse();
    }
}