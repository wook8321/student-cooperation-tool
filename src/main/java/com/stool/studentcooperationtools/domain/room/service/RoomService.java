package com.stool.studentcooperationtools.domain.room.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.participation.Participation;
import com.stool.studentcooperationtools.domain.participation.repository.ParticipationRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomAddRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomPasswordValidRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomRemoveRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomTopicUpdateRequest;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomAddResponse;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomSearchResponse;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomsFindResponse;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.topic.repository.TopicRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final int pageSize = 6;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final ParticipationRepository participationRepository;

    public RoomsFindResponse findRooms(SessionMember member, final int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Room> rooms = roomRepository.findRoomsByMemberIdWithPage(member.getMemberSeq(), pageable);
        return RoomsFindResponse.of(rooms.getContent());
    }

    @Transactional
    public RoomAddResponse addRoom(SessionMember member, final RoomAddRequest request) {
        if(roomRepository.existsByTitle(member.getMemberSeq(), request.getTitle()))
            throw(new IllegalArgumentException("이미 존재하는 방 제목입니다"));
        Member user = memberRepository.findById(member.getMemberSeq())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 등록되어 있지 않습니다"));
        Room room = Room.builder()
                .password(request.getPassword())
                .participationNum(0)
                .title(request.getTitle())
                .leader(user)
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        for(Long participation : request.getParticipation()){
            Member teammate = memberRepository.findById(participation)
                            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 팀원 정보입니다"));
            participationRepository.save(Participation.of(teammate, room));
        }
        return RoomAddResponse.builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .build();
    }

    public RoomSearchResponse searchRoom(final String title, final int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Room> rooms = roomRepository.findRoomsByTitleWithPage(title, pageable);
        return RoomSearchResponse.of(rooms.getContent());
    }

    @Transactional
    public Boolean removeRoom(SessionMember member, final RoomRemoveRequest request) {
        Room room = roomRepository.findRoomByRoomId(member.getMemberSeq(), request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("소속되지 않은 방 정보입니다"));
        if(Objects.equals(member.getMemberSeq(), room.getLeader().getId())){
            roomRepository.deleteById(request.getRoomId());
        }
        else{
            Member teammate = memberRepository.findById(member.getMemberSeq())
                    .orElseThrow(() -> new IllegalArgumentException("유저 정보가 올바르지 않습니다"));
            participationRepository.delete(Participation.of(teammate, room));
        }
        return true;
    }

    public Boolean validRoomPassword(SessionMember member, final RoomPasswordValidRequest request) {
        Room room = roomRepository.findRoomByRoomId(member.getMemberSeq(), request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 방 정보입니다"));
        if(!room.verifyPassword(request.getPassword())) {
            throw new IllegalArgumentException("올바르지 않은 비밀번호입니다");
        }
        Member user = memberRepository.findById(member.getMemberSeq())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 올바르지 않습니다"));
        if(!participationRepository.existsByMemberIdAndRoomId(member.getMemberSeq(), room.getId())){
            participationRepository.save(Participation.of(user, room));
        }
        return true;
    }

    @Transactional
    public Boolean updateRoomTopic(SessionMember member, final RoomTopicUpdateRequest request) {
        Room room = roomRepository.findRoomByRoomId(member.getMemberSeq(), request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 방 정보입니다"));
        room.updateTopic(topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 주제 정보입니다")));
        return true;
    }
}
