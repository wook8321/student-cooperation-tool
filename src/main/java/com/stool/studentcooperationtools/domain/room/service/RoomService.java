package com.stool.studentcooperationtools.domain.room.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final int pageSize = 6;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;

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
                .title(request.getTitle())
                .build();
        roomRepository.save(room);
        return RoomAddResponse.builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .build();
    }

    public RoomSearchResponse searchRoom(SessionMember member, final String title, final int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Room> rooms = roomRepository.findRoomsByTitleWithPage(member.getMemberSeq(), title, pageable);
        return RoomSearchResponse.of(rooms.getContent());
    }

    @Transactional
    public Boolean removeRoom(SessionMember member, final RoomRemoveRequest request) {
        roomRepository.findRoomByRoomId(member.getMemberSeq(), request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 방 정보입니다"));
        roomRepository.deleteById(request.getRoomId());
        return true;
    }

    public Boolean validRoomPassword(SessionMember member, final RoomPasswordValidRequest request) {
        Room room = roomRepository.findRoomByRoomId(member.getMemberSeq(), request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 방 정보입니다"));
        if(!room.verifyPassword(request.getPassword())) {
            throw new IllegalArgumentException("올바르지 않은 비밀번호입니다");
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
