package com.stool.studentcooperationtools.domain.part.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.part.Part;
import com.stool.studentcooperationtools.domain.part.controller.response.PartFindResponse;
import com.stool.studentcooperationtools.domain.part.repository.PartRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.part.request.PartAddWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.part.request.PartDeleteWebsocketRequest;
import com.stool.studentcooperationtools.websocket.controller.part.response.PartAddWebsocketResponse;
import com.stool.studentcooperationtools.websocket.controller.part.response.PartDeleteWebsocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;

    public PartFindResponse findParts(final Long roomId) {
        List<Part> parts = partRepository.findAllByRoomId(roomId);
        return PartFindResponse.of(parts);
    }

    @Transactional
    public PartAddWebsocketResponse addPart(final PartAddWebsocketRequest request, final SessionMember sessionMember) {
        Member member = memberRepository.findById(sessionMember.getMemberSeq())
                .orElseThrow(() -> new IllegalArgumentException("역할을 추가하는 것을 실패했습니다."));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("역할을 추가할 방이 존재하지 않습니다."));

        Part part = Part.builder()
                .partName(request.getPartName())
                .member(member)
                .room(room)
                .build();
        return PartAddWebsocketResponse.of(partRepository.save(part));
    }

    @Transactional(rollbackFor = AccessDeniedException.class)
    public PartDeleteWebsocketResponse deletePart(final PartDeleteWebsocketRequest request, final SessionMember member) {
        int result = partRepository.deletePartByLeaderOrOwner(request.getPartId(), member.getMemberSeq());
        if(result == 0){
            throw new AccessDeniedException("역할을 삭제할 권한이 없습니다.");
        }
        return PartDeleteWebsocketResponse.of(request.getPartId());
    }
}
