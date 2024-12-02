package com.stool.studentcooperationtools.domain.slide.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.slides.v1.Slides;
import com.google.api.services.slides.v1.model.Page;
import com.google.api.services.slides.v1.model.Presentation;
import com.google.api.services.slides.v1.model.Thumbnail;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.script.Script;
import com.stool.studentcooperationtools.domain.script.repository.ScriptRepository;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlideService {
    private final SlideRepository slideRepository;
    private final PresentationRepository presentationRepository;
    private final SlidesFactory slidesFactory;
    private final ScriptRepository scriptRepository;

    public SlideFindResponse findSlides(final Long presentationId) {
        List<Slide> slides = slideRepository.findSlidesAndScriptsByPresentationId(presentationId);
        return SlideFindResponse.of(slides);
    }

    @Transactional
    public boolean updateSlides(Long presentationId, Credential credential) {
        com.stool.studentcooperationtools.domain.presentation.Presentation presentation =
                presentationRepository.findById(presentationId)
                        .orElseThrow(()->new IllegalArgumentException("발표 자료가 없습니다"));
        String presentationPath = presentation.getPresentationPath();
        Slides service = slidesFactory.createSlidesService(credential);
        try {
            Presentation response = service.presentations().get(presentationPath).execute();
            List<Page> slides = response.getSlides();
            List<CompletableFuture<Slide>> futures = slides.stream()
                    .map(slide -> CompletableFuture.supplyAsync(() -> {
                        String objectId = slide.getObjectId();
                        Thumbnail thumbnail = null;
                        try {
                            thumbnail = service.presentations().pages().getThumbnail(presentationPath, objectId).execute();
                        } catch (IOException e) {
                            throw new IllegalStateException(e.getMessage(), e.getCause());
                        }
                        Script script = Script.builder()
                                .script("")
                                .presentation(presentation)
                                .build();
                        return Slide.builder()
                                .slideUrl(objectId)
                                .presentation(presentation)
                                .thumbnail(thumbnail.getContentUrl())
                                .script(script)
                                .build();
                    }))
                    .toList();
        List<Slide> slideList = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        slideRepository.deleteByPresentationId(presentationId);
        slideRepository.saveAll(slideList);
        } catch(IOException e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
        return true;
    }
}
