package com.stool.studentcooperationtools.domain.participation.repository;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.participation.Participation;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@SpringBootTest
class ParticipationRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ParticipationRepository participationRepository;

    @Test
    @DisplayName("유저가 방에 참여했는지 확인")
    void existsByMemberIdAndRoomId() {
        //given
        Member member = Member.builder()
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .nickName("nickName")
                .build();
        memberRepository.save(member);
        Room room = Room.builder()
                .title("t")
                .participationNum(1)
                .leader(member)
                .password("1234")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(member, room));
        //when
        //then
        Assertions.assertThat(participationRepository.existsByMemberIdAndRoomId(member.getId(), room.getId()))
                .isTrue();
    }

    @Test
    @DisplayName("방 id에 따라 해당 방의 모든 참석 정보 삭제")
    void deleteParticipationByRoomId() {
        //given
        Member member = Member.builder()
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .nickName("nickName")
                .build();
        memberRepository.save(member);
        Member member2 = Member.builder()
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .nickName("nickName")
                .build();
        memberRepository.save(member2);
        Room room = Room.builder()
                .title("t")
                .participationNum(1)
                .leader(member)
                .password("1234")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(member, room));
        participationRepository.save(Participation.of(member2, room));
        //when
        //then
        participationRepository.deleteByRoomId(room.getId());
        Assertions.assertThat(participationRepository.existsByRoomId(room.getId()))
                .isFalse();
    }

    @Test
    @DisplayName("방에서 해당 유저의 참석 정보 삭제")
    void deleteParticipationByUserIdAndRoomId() {
        //given
        Member member = Member.builder()
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .nickName("nickName")
                .build();
        memberRepository.save(member);
        Member member2 = Member.builder()
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .nickName("nickName")
                .build();
        memberRepository.save(member2);
        Room room = Room.builder()
                .title("t")
                .participationNum(1)
                .leader(member)
                .password("1234")
                .build();
        roomRepository.save(room);
        participationRepository.save(Participation.of(member, room));
        participationRepository.save(Participation.of(member2, room));
        //when
        //then
        participationRepository.deleteByMemberIdAndRoomId(member.getId(), room.getId());
        Assertions.assertThat(participationRepository.existsByMemberIdAndRoomId(member.getId(), room.getId()))
                .isFalse();
    }
}