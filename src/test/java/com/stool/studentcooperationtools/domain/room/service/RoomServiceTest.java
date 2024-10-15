package com.stool.studentcooperationtools.domain.room.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.participation.Participation;
import com.stool.studentcooperationtools.domain.participation.repository.ParticipationRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomAddRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomPasswordValidRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomRemoveRequest;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomTopicUpdateRequest;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomAddResponse;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomSearchDto;
import com.stool.studentcooperationtools.domain.room.controller.response.RoomSearchResponse;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.topic.repository.TopicRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class RoomServiceTest {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ParticipationRepository participationRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoomService roomService;
    @Autowired
    TopicRepository topicRepository;

    @BeforeEach
    void setUp(){
        participationRepository.deleteAll();
        memberRepository.deleteAll();
        roomRepository.deleteAll();
        topicRepository.deleteAll();
    }

    @Test
    @DisplayName("방이 있을 때 방 목록 조회")
    void findAllByMemberIdWithRoom() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .leader(user)
                .participationNum(1)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        //when
        //then
        assertThat(roomService.findRooms(member, 0).getNum()).isGreaterThan(0);
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
        //when
        //then
        assertThat(roomService.findRooms(member, 0).getNum()).isEqualTo(0);
    }

    @Test
    @DisplayName("제목이 중복되는 방 생성 시 에러 발생")
    void addRoomWithDuplicateTitle() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .password("password")
                .leader(user)
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        RoomAddRequest request = RoomAddRequest.builder()
                .title("room")
                .participation(List.of(user.getId()))
                .password("1234")
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> roomService.addRoom(member, request));
    }

    @Test
    @DisplayName("unique한 제목과 유효한 유저 정보로 방 생성")
    void addRoomWithUniqueTitleByValidUser() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        RoomAddRequest request = RoomAddRequest.builder()
                .title("room")
                .participation(List.of(user.getId()))
                .password("1234")
                .build();
        //when
        RoomAddResponse roomAddResponse = roomService.addRoom(member, request);
        //then
        assertThat(roomAddResponse.getTitle()).isEqualTo("room");
    }

    @Test
    @DisplayName("검색 제목에 해당하는 방이 있을 때 검색 결과")
    void searchRoomByValidTitle() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(user)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        //when
        RoomSearchResponse roomSearchResponse = roomService.searchRoom("room", 0);
        List<RoomSearchDto> rooms = roomSearchResponse.getRooms();
        //then
        assertThat(rooms.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("검색 제목에 해당하는 방이 없을 때 검색 결과")
    void searchRoomByInValidTitle() {
        //given
        String invalidTitle = "invalidTitle";
        //when
        RoomSearchResponse roomSearchResponse = roomService.searchRoom(invalidTitle, 0);
        List<RoomSearchDto> rooms = roomSearchResponse.getRooms();
        //then
        assertThat(rooms.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("소속되지 않은 방에 대해 삭제 요청 시 에러")
    void removeNotBelongingRoom() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Member leader = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(leader);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(leader)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(leader, room));
        RoomRemoveRequest roomRemoveRequest = RoomRemoveRequest.builder()
                .roomId(room.getId())
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> roomService.removeRoom(member, roomRemoveRequest));
    }

    @Test
    @DisplayName("팀장이 방 삭제 요청 시 방을 삭제")
    void removeRoomByLeader() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(user)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        RoomRemoveRequest roomRemoveRequest = RoomRemoveRequest.builder()
                .roomId(room.getId())
                .build();
        //when
        roomService.removeRoom(member, roomRemoveRequest);
        //then
        assertThat(roomRepository.existsById((room.getId()))).isFalse();
    }

    @Test
    @DisplayName("팀원이 방 삭제 요청 시 방 참여 인원에서 삭제")
    void removeRoomByTeamMate() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        Member leader = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(leader);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(leader)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        participationRepository.save(Participation.of(leader, room));
        RoomRemoveRequest roomRemoveRequest = RoomRemoveRequest.builder()
                .roomId(room.getId())
                .build();
        //when
        roomService.removeRoom(member, roomRemoveRequest);
        //then
        assertThat(roomRepository.existsById((room.getId()))).isTrue();
        assertThat(participationRepository.existsByMemberIdAndRoomId(user.getId(), room.getId())).isFalse();
    }

    @Test
    @DisplayName("요청한 방의 정보가 올바르지 않을 때 에러")
    void enterInvalidRoom(){
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(user)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        RoomPasswordValidRequest request = RoomPasswordValidRequest.builder()
                .roomId(10L)
                .password("password")
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, ()-> roomService.validRoomPassword(member, request));
    }

    @Test
    @DisplayName("방 비밀번호를 틀렸을 때 에러")
    void enterRoomWithInvalidPassword() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(user)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        RoomPasswordValidRequest request = RoomPasswordValidRequest.builder()
                .roomId(room.getId())
                .password("123")
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, ()-> roomService.validRoomPassword(member, request));
    }

    @Test
    @DisplayName("기존에 없던 방에 유효한 비밀번호로 입장 시 participation 추가")
    void enterNewRoomWithValidPassword() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Member leader = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(leader);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(leader)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(leader, room));
        RoomPasswordValidRequest request = RoomPasswordValidRequest.builder()
                .roomId(room.getId())
                .password(room.getPassword())
                .build();
        //when
        roomService.validRoomPassword(member, request);
        //then
        assertThat(participationRepository.existsByMemberIdAndRoomId(user.getId(), room.getId())).isTrue();
        assertThat(roomRepository.findById(room.getId()).get().getParticipationNum()).isEqualTo(2);
    }

    @Test
    @DisplayName("속해있던 방에 유효한 비밀번호로 입장 시 return true")
    void enterRoomWithValidPassword() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(user)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(user, room));
        RoomPasswordValidRequest request = RoomPasswordValidRequest.builder()
                .roomId(room.getId())
                .password(room.getPassword())
                .build();
        //when
        //then
        assertThat(roomService.validRoomPassword(member, request)).isTrue();
    }

    @Test
    @DisplayName("주제 추가 request에 잘못된 방 정보 담겨왔을 때 에러")
    void updateRoomTopicWithInvalidRoomId() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(user)
                .password("password")
                .build();
        roomRepository.save(room);
        Topic topic = Topic.builder()
                        .room(room)
                        .topic("t")
                        .member(user)
                        .build();
        participationRepository.save(Participation.of(user, room));
        topicRepository.save(topic);
        RoomTopicUpdateRequest request = RoomTopicUpdateRequest.builder()
                .roomId(10L)
                .topicId(topic.getId())
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> roomService.updateRoomTopic(member, request));
    }

    @Test
    @DisplayName("주제 추가 request에 잘못된 주제 정보 가져왔을 때 에러")
    void updateRoomTopicWithInvalidTopicId() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(user)
                .password("password")
                .build();
        roomRepository.save(room);
        Topic topic = Topic.builder()
                .room(room)
                .topic("t")
                .member(user)
                .build();
        participationRepository.save(Participation.of(user, room));
        topicRepository.save(topic);
        RoomTopicUpdateRequest request = RoomTopicUpdateRequest.builder()
                .roomId(room.getId())
                .topicId(10L)
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> roomService.updateRoomTopic(member, request));
    }

    @Test
    @DisplayName("팀장이 아닌 사람이 주제 등록 할 때 에러")
    void updateRoomTopicByTeamMate() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Member leader = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(leader);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(leader)
                .password("password")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(leader, room));
        Topic topic = Topic.builder()
                .room(room)
                .topic("t")
                .member(user)
                .build();
        participationRepository.save(Participation.of(user, room));
        topicRepository.save(topic);
        RoomTopicUpdateRequest request = RoomTopicUpdateRequest.builder()
                .roomId(room.getId())
                .topicId(topic.getId())
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> roomService.updateRoomTopic(member, request));
    }

    @Test
    @DisplayName("팀장이 선정된 주제를 업데이트")
    void updateRoomTopicByLeader() {
        //given
        Member user = Member.builder()
                .role(Role.USER)
                .email("email")
                .profile("profile")
                .nickName("nickName")
                .build();
        memberRepository.save(user);
        SessionMember member = SessionMember.of(user);
        Room room = Room.builder()
                .title("room")
                .participationNum(1)
                .leader(user)
                .password("password")
                .build();
        roomRepository.save(room);
        Topic topic = Topic.builder()
                .room(room)
                .topic("t")
                .member(user)
                .build();
        participationRepository.save(Participation.of(user, room));
        topicRepository.save(topic);
        RoomTopicUpdateRequest request = RoomTopicUpdateRequest.builder()
                .roomId(room.getId())
                .topicId(topic.getId())
                .build();
        //when
        roomService.updateRoomTopic(member, request);
        //then
        assertThat(room.getTopic()).isEqualTo(topic.getTopic());
    }

    @Test
    @DisplayName("동시에 방 입장 동시성 제어")
    void enterRoomConcurrencyControl() throws InterruptedException {
        //given
        Room room = Room.builder()
                .title("room")
                .participationNum(0)
                .password("password")
                .build();
        roomRepository.save(room);
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(100);
        for(int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                try{
                    Member member = Member.builder()
                                    .email("e")
                                    .profile("p")
                                    .role(Role.USER)
                                    .nickName("n")
                                    .build();
                    memberRepository.save(member);
                    SessionMember sessionMember = SessionMember.of(member);
                    RoomPasswordValidRequest request = RoomPasswordValidRequest
                            .builder().roomId(room.getId()).password("password").build();
                    roomService.validRoomPassword(sessionMember, request);
                }finally{
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();
        //when
        Room findRoom = roomRepository.findById(room.getId()).get();
        //then
        assertThat(findRoom.getParticipationNum()).isEqualTo(100);
    }
}

