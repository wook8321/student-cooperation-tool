package com.stool.studentcooperationtools.domain.presentation.repository;

import com.stool.studentcooperationtools.IntegrationTest;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class PresentationRepositoryTest extends IntegrationTest {

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

    @DisplayName("방의 id를 받아 presentaion id를 조회한다.")
    @Test
    void findPresentationIdByRoomId(){
        //given
        Room room = Room.builder()
                .participationNum(1)
                .password("password")
                .title("title")
                .build();
        roomRepository.save(room);
        Presentation presentation = Presentation.builder()
                .presentationPath("path")
                .room(room)
                .build();
        presentationRepository.save(presentation);

        //when
        Long result = presentationRepository.findPresentationIdByRoomId(room.getId());
        //then
        assertThat(presentation.getId()).isEqualTo(result);
    }

}