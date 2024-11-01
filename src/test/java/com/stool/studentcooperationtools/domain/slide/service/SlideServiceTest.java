package com.stool.studentcooperationtools.domain.slide.service;

import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.script.Script;
import com.stool.studentcooperationtools.domain.script.repository.ScriptRepository;
import com.stool.studentcooperationtools.domain.slide.Slide;
import com.stool.studentcooperationtools.domain.slide.controller.response.SlideFindResponse;
import com.stool.studentcooperationtools.domain.slide.repository.SlideRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SlideServiceTest {

    @Autowired
    private SlideService slideService;
    @Autowired
    private SlideRepository slideRepository;
    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private ScriptRepository scriptRepository;

    @Test
    @DisplayName("ppt의 슬라이드를 찾아 response 반환")
    void findSlides() {
        //given
        Presentation presentation = Presentation.builder()
                .presentationPath("path")
                .build();
        presentationRepository.save(presentation);
        Script script = Script.builder()
                .script("script")
                .presentation(presentation)
                .build();
        scriptRepository.save(script);
        Slide slide = Slide.builder()
                .slideUrl("url")
                .presentation(presentation)
                .thumbnail("thumbnail")
                .script(script)
                .build();
        slideRepository.save(slide);
        //when
        SlideFindResponse response = slideService.findSlides(presentation.getId());
        //then
        assertThat(response.getNum()).isEqualTo(1);
    }
}