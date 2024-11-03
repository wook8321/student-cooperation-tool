package com.stool.studentcooperationtools.domain.part.service;

import com.stool.studentcooperationtools.domain.part.Part;
import com.stool.studentcooperationtools.domain.part.controller.response.PartFindResponse;
import com.stool.studentcooperationtools.domain.part.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;

    public PartFindResponse findParts(final Long roomId) {
        List<Part> parts = partRepository.findAllByRoomId(roomId);
        return PartFindResponse.of(parts);
    }
}
