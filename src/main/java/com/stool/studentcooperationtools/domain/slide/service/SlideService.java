package com.stool.studentcooperationtools.domain.slide.service;

import com.stool.studentcooperationtools.domain.script.Script;
import com.stool.studentcooperationtools.domain.script.repository.ScriptRepository;
import com.stool.studentcooperationtools.domain.slide.Slide;
import com.stool.studentcooperationtools.domain.slide.controller.response.SlideFindResponse;
import com.stool.studentcooperationtools.domain.slide.repository.SlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlideService {
    private final SlideRepository slideRepository;

    public SlideFindResponse findSlides(final Long presentationId) {
        List<Slide> slides = slideRepository.findSlidesAndScriptsByPresentationId(presentationId);
        return SlideFindResponse.of(slides);
    }
}
