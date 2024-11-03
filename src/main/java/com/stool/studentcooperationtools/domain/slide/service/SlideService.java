package com.stool.studentcooperationtools.domain.slide.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.slides.v1.Slides;
import com.google.api.services.slides.v1.model.Page;
import com.google.api.services.slides.v1.model.Presentation;
import com.google.api.services.slides.v1.model.Thumbnail;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.slide.Slide;
import com.stool.studentcooperationtools.domain.slide.SlidesFactory;
import com.stool.studentcooperationtools.domain.slide.controller.response.SlideFindResponse;
import com.stool.studentcooperationtools.domain.slide.repository.SlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlideService {
    private final SlideRepository slideRepository;
    private final PresentationRepository presentationRepository;
    private final SlidesFactory slidesFactory;

    public SlideFindResponse findSlides(final Long presentationId) {
        List<Slide> slides = slideRepository.findSlidesAndScriptsByPresentationId(presentationId);
        return SlideFindResponse.of(slides);
    }

    public boolean updateSlides(Long presentationId, Credential credential) throws IOException, GeneralSecurityException {
        com.stool.studentcooperationtools.domain.presentation.Presentation presentation =
                presentationRepository.findById(presentationId)
                        .orElseThrow(()->new IllegalArgumentException("발표 자료가 없습니다"));
        String presentationPath = presentation.getPresentationPath();
        Slides service = slidesFactory.createSlidesService(credential);
        Presentation response = service.presentations().get(presentationPath).execute();
        List<Page> slides = response.getSlides();
        List<Slide> slideList = new ArrayList<>();
        for (int i = 0; i < slides.size(); ++i) {
            String objectId = slides.get(i).getObjectId();
            Thumbnail thumbnail = service.presentations().pages().getThumbnail(presentationPath, objectId).execute();
            Slide slide = Slide.builder()
                    .slideUrl(objectId)
                    .presentation(presentation)
                    .thumbnail(thumbnail.getContentUrl())
                    .build();
            slideList.add(slide);
        }
        slideRepository.saveAll(slideList);
        return true;
    }
}
