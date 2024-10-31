package com.stool.studentcooperationtools.domain.room.repository;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.participation.Participation;
import com.stool.studentcooperationtools.domain.participation.repository.ParticipationRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ParticipationRepository participationRepository;

    @Test
    @DisplayName("방이 있을 때 방 목록 조회")
    void findAllByMemberIdWithRoom() {
        //given
        Room room = Room.builder()
                        .title("room")
                        .participationNum(1)
                        .password("password")
                        .build();
        Member user = Member.builder()
                        .role(Role.USER)
                        .email("email")
                        .profile("profile")
                        .nickName("nickName")
                        .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        Pageable pageable = PageRequest.of(0, 1);
        //when
        Page<Room> rooms = roomRepository.findRoomsByMemberIdWithPage(member.getMemberSeq(), pageable);
        //then
        assertThat(rooms.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("방이 없을 때 방 목록 조회")
    void findAllByMemberIdWithOutRoom() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Pageable pageable = PageRequest.of(0, 1);
        Page<Room> rooms = roomRepository.findRoomsByMemberIdWithPage(member.getMemberSeq(), pageable);
        //when
        //then
        assertThat(rooms.getContent()).isEmpty();
    }

    @Test
    @DisplayName("방 목록 조회 시 방 생성 순 정렬")
    void findRoomsByWithUpdatedTimeSorting() {
        //given
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .password("password")
                .build();
        Room room2 = Room.builder()
                .title("room2")
                .participationNum(1)
                .password("password")
                .build();
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        roomRepository.saveAll(List.of(room, room2));
        participationRepository.saveAll(List.of(Participation.of(user, room), Participation.of(user, room2)));
        Pageable pageable = PageRequest.of(0, 2);
        //when
        Page<Room> rooms = roomRepository.findRoomsByMemberIdWithPage(member.getMemberSeq(), pageable);
        //then
        assertThat(rooms.getContent().get(0).getId()).isGreaterThan(rooms.getContent().get(1).getId());
    }
    @Test
    @DisplayName("방이 pageSize 넘어갈 시 페이지 넘어가서 조회 되는지")
    void findRoomToNextPage() {
        //given
        Room room = Room.builder()
                .title("room")
                .participationNum(0)
                .password("password")
                .build();
        Room room2 = Room.builder()
                .title("room2")
                .participationNum(1)
                .password("password")
                .build();
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        roomRepository.saveAll(List.of(room, room2));
        participationRepository.saveAll(List.of(Participation.of(user, room), Participation.of(user,room2)));
        Pageable pageable = PageRequest.of(1, 1);
        //when
        Page<Room> rooms = roomRepository.findRoomsByMemberIdWithPage(member.getMemberSeq(), pageable);
        //then
        assertThat(rooms.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("유효한 제목으로 방 목록 조회")
    void findRoomsWithValidTitle() {
        //given
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .password("password")
                .build();
        roomRepository.save(room);
        Pageable pageable = PageRequest.of(0, 1);
        //when
        Page<Room> rooms = roomRepository.findRoomsByTitleWithPage(room.getTitle(), pageable);
        //then
        assertThat(rooms.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("유효하지 않은 제목으로 방 목록 조회")
    void findRoomsWithInValidTitle() {
        //given
        String invalidTitle = "invalidTitle";
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .password("password")
                .build();
        roomRepository.save(room);
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        Pageable pageable = PageRequest.of(0, 1);
        //when
        Page<Room> rooms = roomRepository.findRoomsByTitleWithPage(invalidTitle, pageable);
        //then
        assertThat(rooms.getContent()).isEmpty();
    }

    @Test
    @DisplayName("유효한 방 id로 방 검색")
    void findRoomWithValidRoomId() {
        //given
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .password("password")
                .build();
        roomRepository.save(room);
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        participationRepository.save(Participation.of(user, room));
        //when
        //then
        assertThat(roomRepository.findRoomByRoomId(member.getMemberSeq(), room.getId())
                .orElseThrow(() -> new IllegalArgumentException("방 정보 오류")).getId()).isEqualTo(room.getId());
    }

    @Test
    @DisplayName("유효하지 않은 방 id로 방 검색")
    void findRoomWithInValidRoomId() {
        //given
        Long invalidRoomId = Long.MAX_VALUE;
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .password("password")
                .build();
        roomRepository.save(room);
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        participationRepository.save(Participation.of(user, room));
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> roomRepository.findRoomByRoomId(member.getMemberSeq(), invalidRoomId)
                .orElseThrow(() -> new IllegalArgumentException("방 정보 오류")));
    }
}