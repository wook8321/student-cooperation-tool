package com.stool.studentcooperationtools.domain.presentation.service;

import com.stool.studentcooperationtools.IntegrationTest;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.presentation.controller.response.PresentationFindResponse;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.presentation.request.PresentationUpdateSocketRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class PresentationServiceTest extends IntegrationTest {

    @Autowired
    private PresentationService presentationService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("방의 발표 자료를 찾아 response 보내기")
    void findPresentation() {
        //given
        Room room = Room.builder()
                .participationNum(1)
                .password("1234")
                .title("t")
                .build();
        roomRepository.save(room);
        Presentation presentation = Presentation.builder()
                .presentationPath("path")
                .room(room)
                .build();
        presentationRepository.save(presentation);
        //when
        PresentationFindResponse response = presentationService.findPresentation(room.getId());
        //then
        assertThat(response.getPresentationPath()).isEqualTo("path");
    }

    @Test
    @DisplayName("방장이 ppt를 변경")
    void updatePresentation(){
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("e")
                .profile("p")
                .nickName("n")
                .build();
        Room room = Room.builder()
                .participationNum(1)
                .password("1234")
                .title("t")
                .leader(user)
                .build();
        roomRepository.save(room);
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Presentation presentation = Presentation.builder()
                .presentationPath("path")
                .room(room)
                .build();
        presentationRepository.save(presentation);
        PresentationUpdateSocketRequest request = PresentationUpdateSocketRequest.builder()
                .roomId(room.getId())
                .presentationPath("newPath")
                .build();
        //when
        presentationService.updatePresentation(request, member);
        Presentation updatedPpt = presentationRepository.findByRoomId(room.getId())
                .orElseThrow(()->new IllegalArgumentException("방에 ppt가 존재하지 않습니다."));
        //then
        Assertions.assertEquals(updatedPpt.getPresentationPath(), "newPath");
    }

    @Test
    @DisplayName("방장이 아닌 팀원이 ppt 업데이트 시 에러 발생")
    void updatePresentationByNonLeader(){
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("e")
                .profile("p")
                .nickName("n")
                .build();
        Room room = Room.builder()
                .participationNum(1)
                .leader(user)
                .password("1234")
                .title("t")
                .build();
        roomRepository.save(room);
        memberRepository.save(user);
        Long otherUserId = 2L;
        SessionMember member = SessionMember.builder()
                .memberSeq(otherUserId)
                .nickName("other")
                .profile("p")
                .build();
        PresentationUpdateSocketRequest request = PresentationUpdateSocketRequest.builder()
                .roomId(room.getId())
                .presentationPath("path")
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> presentationService.updatePresentation(request, member));
    }
}