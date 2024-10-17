package com.stool.studentcooperationtools.domain.presentation.service;

import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.presentation.controller.response.PresentationFindResponse;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PresentationServiceTest {

    @Autowired
    private PresentationService presentationService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private PresentationRepository presentationRepository;

    @Test
    @DisplayName("방의 발표 자료을 찾아 response 보내기")
    void findPresentation() {
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
        PresentationFindResponse response = presentationService.findPresentation(room.getId());
        //then
        assertThat(response.getPresentationPath()).isEqualTo("path");
    }
}