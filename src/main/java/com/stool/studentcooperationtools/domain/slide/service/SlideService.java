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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            List<CompletableFuture<Slide>> futures = IntStream.range(0, slides.size())
                    .mapToObj(index -> CompletableFuture.supplyAsync(() -> {
                        Page slide = slides.get(index); // 해당 인덱스의 슬라이드 가져오기
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
                                .slide_index(index)
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

    public boolean compareSlides(Long presentationId, Credential credential) {
        com.stool.studentcooperationtools.domain.presentation.Presentation presentation =
                presentationRepository.findById(presentationId)
                        .orElseThrow(() -> new IllegalArgumentException("발표 자료가 없습니다"));
        String presentationPath = presentation.getPresentationPath();
        Slides service = slidesFactory.createSlidesService(credential);
        Presentation response;
        try {
            response = service.presentations().get(presentationPath).execute();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
        List<Page> slides = response.getSlides();
        List<Slide> newSlides = IntStream.range(0, slides.size())
                .mapToObj(index -> {
                    Page slide = slides.get(index);
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
                            .script(script)
                            .presentation(presentation)
                            .slideUrl(objectId)
                            .thumbnail(thumbnail.getContentUrl())
                            .slide_index(index)
                            .build();
                })
                .toList();

        List<Slide> existingSlides = slideRepository.findSlidesAndScriptsByPresentationId(presentationId);
        Map<String, Slide> existingSlideMap = existingSlides.stream()
                .collect(Collectors.toMap(Slide::getSlideUrl, slide -> slide));
        List<Slide> slidesToSave = new ArrayList<>();
        List<Slide> slidesToDelete = new ArrayList<>(existingSlides);
        for (int index = 0; index < newSlides.size(); index++) {
            Slide newSlide = newSlides.get(index);
            Slide existingSlide = existingSlideMap.get(newSlide.getSlideUrl());
            if (existingSlide != null) {
                existingSlide.updateIndex(index);
                slidesToSave.add(existingSlide);
                slidesToDelete.remove(existingSlide);
            }
            else{
                slidesToSave.add(newSlide);
            }
        }
        slideRepository.saveAll(slidesToSave);
        slideRepository.deleteAll(slidesToDelete);
            return true;
    }
}
