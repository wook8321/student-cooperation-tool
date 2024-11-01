package com.stool.studentcooperationtools.domain.presentation.repository;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PresentationRepositoryTest {

    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("방의 presentation 정보 찾기")
    void findPresentationByRoomId() {
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
        Presentation findPpt = presentationRepository.findByRoomId(room.getId())
                .orElseThrow(()->new IllegalArgumentException("잘못된 ppt 정보"));
        //then
        assertThat(findPpt.getPresentationPath()).isEqualTo("path");
    }

    @Test
    @DisplayName("방의 presentation 정보가 유효하지 않으면 에러")
    void findInvalidPresentationByRoomId() {
        //given
        Room room = Room.builder()
                .participationNum(1)
                .password("1234")
                .title("t")
                .build();
        roomRepository.save(room);
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> presentationRepository.findByRoomId(room.getId())
                .orElseThrow(()->new IllegalArgumentException("잘못된 ppt 정보")));
    }

    @Test
    @DisplayName("방에 ppt가 있을 때 exist 조회")
    void existPresentationByRoomId() {
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
        //then
        assertThat(presentationRepository.existsByRoomId(room.getId())).isTrue();
    }

    @Test
    @DisplayName("방에 ppt가 없을 때 exist 조회")
    void existNoPresentationByRoomId() {
        //given
        Room room = Room.builder()
                .participationNum(1)
                .password("1234")
                .title("t")
                .build();
        roomRepository.save(room);
        //when
        //then
        assertThat(presentationRepository.existsByRoomId(room.getId())).isFalse();
    }

    @Test
    @DisplayName("방장에 의한 방의 ppt 주소 변경")
    void updatePresentation() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("e")
                .profile("p")
                .nickName("n")
                .build();
        memberRepository.save(user);
        Room room = Room.builder()
                .participationNum(1)
                .leader(user)
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
        int cnt = presentationRepository.updatePresentationByLeader("newPath", user.getId());
        Presentation updatedPresentation = presentationRepository.findById(presentation.getId())
                .orElseThrow();
        //then
        assertAll(
                ()->assertThat(cnt).isEqualTo(1),
                ()->assertThat(updatedPresentation.getPresentationPath()).isEqualTo("newPath")
                );
    }

}