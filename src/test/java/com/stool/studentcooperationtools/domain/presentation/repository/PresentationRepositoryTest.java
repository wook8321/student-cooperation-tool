package com.stool.studentcooperationtools.domain.presentation.repository;

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

    @Test
    @DisplayName("방의 presentation 정보 찾기")
    void findPresentationByRoomId() {
        //given
        Room room = Room.builder()
                        .participationNum(0)
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
                .participationNum(0)
                .password("1234")
                .title("t")
                .build();
        roomRepository.save(room);
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> presentationRepository.findByRoomId(room.getId())
                .orElseThrow(()->new IllegalArgumentException("잘못된 ppt 정보")));
    }
}