package com.stool.studentcooperationtools.domain.slide.repository;

import com.stool.studentcooperationtools.IntegrationTest;
import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.script.Script;
import com.stool.studentcooperationtools.domain.script.repository.ScriptRepository;
import com.stool.studentcooperationtools.domain.slide.Slide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SlideRepositoryTest extends IntegrationTest {

    @Autowired
    private SlideRepository slideRepository;
    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private ScriptRepository scriptRepository;

    @Test
    @DisplayName("ppt의 유효한 슬라이드와 스크립트 찾기")
    void findSlidesByPresentationId() {
        //given
        Presentation presentation = Presentation.builder()
                .presentationPath("path")
                .build();
        presentationRepository.save(presentation);
        Script script = Script.builder()
                .script("s")
                .presentation(presentation)
                .build();
        Slide slide = Slide.builder()
                .slideUrl("url")
                .presentation(presentation)
                .thumbnail("thumbnail")
                .script(script)
                .build();
        slideRepository.save(slide);
        //when
        List<Slide> slides = slideRepository.findSlidesAndScriptsByPresentationId(presentation.getId());
        Slide findSlide = slides.get(0);
        //then
        assertEquals(slides.size(), 1);
        assertEquals(findSlide.getSlideUrl(), "url");
        assertEquals(findSlide.getScript().getId(), script.getId());
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
        List<Slide> slides = slideRepository.findSlidesAndScriptsByPresentationId(presentation.getId());
        //then
        assertEquals(slides.size(), 0);
    }
}