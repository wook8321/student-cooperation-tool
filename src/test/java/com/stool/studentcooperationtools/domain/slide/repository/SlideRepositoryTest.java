package com.stool.studentcooperationtools.domain.slide.repository;

import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.slide.Slide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SlideRepositoryTest {

    @Autowired
    private SlideRepository slideRepository;
    @Autowired
    private PresentationRepository presentationRepository;

    @Test
    @DisplayName("ppt의 유효한 슬라이드 찾기")
    void findSlidesByPresentationId() {
        //given
        Presentation presentation = Presentation.builder()
                .presentationPath("path")
                .build();
        presentationRepository.save(presentation);
        Slide slide = Slide.builder()
                .slideUrl("url")
                .presentation(presentation)
                .thumbnail("thumbnail")
                .build();
        slideRepository.save(slide);
        //when
        List<Slide> slides = slideRepository.findByPresentationId(presentation.getId());
        //then
        assertEquals(slides.size(), 1);
    }


    @Test
    @DisplayName("ppt의 유효하지 않은 슬라이드 찾기")
    void findSlidesByInvalidPresentationId() {
        //given
        Presentation presentation = Presentation.builder()
                .presentationPath("path")
                .build();
        presentationRepository.save(presentation);
        //when
        List<Slide> slides = slideRepository.findByPresentationId(presentation.getId());
        //then
        assertEquals(slides.size(), 0);
    }
}