package com.stool.studentcooperationtools.domain.presentation.service;

import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.presentation.controller.response.PresentationFindResponse;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PresentationService {

    private final PresentationRepository presentationRepository;

    public PresentationFindResponse findPresentation(final Long roomId) {
        Presentation presentation = presentationRepository.findByRoomId(roomId)
                .orElseThrow(()-> new IllegalArgumentException("잘못된 발표자료 정보"));
        return PresentationFindResponse.builder()
                .presentationId(presentation.getId())
                .presentationPath(presentation.getPresentationPath())
                .build();
    }
}
