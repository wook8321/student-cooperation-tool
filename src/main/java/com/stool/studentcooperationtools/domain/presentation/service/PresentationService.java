package com.stool.studentcooperationtools.domain.presentation.service;

import com.stool.studentcooperationtools.domain.presentation.Presentation;
import com.stool.studentcooperationtools.domain.presentation.controller.response.PresentationFindResponse;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.presentation.request.PresentationUpdateSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.presentation.response.PresentationUpdateSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final RoomRepository roomRepository;

    public PresentationFindResponse findPresentation(final Long roomId) {
        Presentation presentation = presentationRepository.findByRoomId(roomId)
                .orElseThrow(()-> new IllegalArgumentException("해당 방의 발표자료가 존재하지 않습니다"));
        return PresentationFindResponse.builder()
                .presentationId(presentation.getId())
                .presentationPath(presentation.getPresentationPath())
                .build();
    }

    @Transactional
    public PresentationUpdateSocketResponse updatePresentation(final PresentationUpdateSocketRequest request, SessionMember member) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(()->new IllegalArgumentException("해당 방은 존재하지 않습니다"));
        if(!room.getLeader().getId().equals(member.getMemberSeq())){
            throw new IllegalArgumentException("발표자료 변경 권한이 없습니다");
        }
        if(!presentationRepository.existsByRoomId(room.getId())) {
            Presentation presentation = Presentation.builder()
                    .room(room)
                    .presentationPath(request.getPresentationPath())
                    .build();
            presentationRepository.save(presentation);
            return PresentationUpdateSocketResponse.of(presentation);
        }
        Presentation updatingPpt = presentationRepository.findByRoomId(room.getId())
                .orElseThrow(()->new IllegalArgumentException("발표자료가 존재하지 않습니다"));
        updatingPpt.updatePath(request.getPresentationPath());
        return PresentationUpdateSocketResponse.of(updatingPpt);
    }
}
