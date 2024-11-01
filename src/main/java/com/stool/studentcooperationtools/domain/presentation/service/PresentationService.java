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
        Presentation presentation = Presentation.builder()
                .room(room)
                .presentationPath(request.getPresentationPath())
                .build();
        if(!presentationRepository.existsByRoomId(room.getId())) {
            if(!room.getLeader().getId().equals(member.getMemberSeq())) {
                throw new IllegalArgumentException("ppt를 추가할 수 없습니다");
            }
            presentationRepository.save(presentation);
        }
        else if(presentationRepository.updatePresentationByLeader(request.getPresentationPath(), member.getMemberSeq())==0)
            throw new IllegalArgumentException("ppt를 변경할 수 없습니다");
        return PresentationUpdateSocketResponse.of(presentation);
    }
}
