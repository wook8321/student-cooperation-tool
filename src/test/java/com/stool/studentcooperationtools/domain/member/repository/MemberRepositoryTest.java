package com.stool.studentcooperationtools.domain.member.repository;

import com.stool.studentcooperationtools.domain.friendship.Friendship;
import com.stool.studentcooperationtools.domain.friendship.repository.FriendshipRepository;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    @Test
    @DisplayName("사용자의 id로 친구 목록 조회")
    void findFriendsByMemberId() {
        //given
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();

        Member memberB = Member.builder()
                .email("emailB")
                .profile("profileB")
                .nickName("nickB")
                .role(Role.USER)
                .build();
        memberRepository.saveAll(List.of(memberA, memberB));
        friendshipRepository.save(Friendship.of(memberA, memberB));
        //when
        List<Member> friendList = memberRepository.findFriendsByMemberId(memberA.getId());
        //then
        assertThat(friendList.get(0).getId()).isEqualTo(memberB.getId());

    }

    @Test
    @DisplayName("유효한 닉네임으로 해당 친구가 있는 유저의 친구 목록 조회")
    void findFriendsByValidFriendNickName() {
        //given
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();

        Member memberB = Member.builder()
                .email("emailB")
                .profile("profileB")
                .nickName("nickB")
                .role(Role.USER)
                .build();
        memberRepository.saveAll(List.of(memberA, memberB));
        friendshipRepository.save(Friendship.of(memberA, memberB));
        //when
        List<Member> friendList = memberRepository.findFriendsByMemberNickName(memberB.getNickName(),
                memberA.getId());
        //then
        assertThat(friendList.get(0).getId()).isEqualTo(memberB.getId());
    }

    @Test
    @DisplayName("유효한 닉네임으로 해당 친구가 없는 유저의 친구 목록 조회")
    void findFriendsByValidMemberNickName() {
        //given
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();

        Member memberB = Member.builder()
                .email("emailB")
                .profile("profileB")
                .nickName("nickB")
                .role(Role.USER)
                .build();
        memberRepository.saveAll(List.of(memberA, memberB));
        //when
        List<Member> friendList = memberRepository.findFriendsByMemberNickName(memberB.getNickName(),
                memberA.getId());
        //then
        assertThat(friendList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 닉네임으로 친구 목록 조회")
    void findFriendsByInvalidMemberNickName() {
        //given
        String invalidNickName = "invalidNickName";
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();

        Member memberB = Member.builder()
                .email("emailB")
                .profile("profileB")
                .nickName("nickB")
                .role(Role.USER)
                .build();
        memberRepository.saveAll(List.of(memberA, memberB));
        friendshipRepository.save(Friendship.of(memberA, memberB));
        //when
        List<Member> friendList = memberRepository.findFriendsByMemberNickName(invalidNickName,
                memberA.getId());
        //then
        assertThat(friendList.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("유효한 닉네임으로 해당 친구가 없는 유저의 친구가 아닌 목록 조회")
    void findNonFriendsByValidMemberNickName() {
        //given
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();

        Member memberB = Member.builder()
                .email("emailB")
                .profile("profileB")
                .nickName("nickB")
                .role(Role.USER)
                .build();
        memberRepository.saveAll(List.of(memberA, memberB));
        //when
        List<Member> nonfriendList = memberRepository.findNonFriendsByMemberNickName(memberB.getNickName(),
                memberA.getId());
        //then
        assertThat(nonfriendList.get(0).getId()).isEqualTo(memberB.getId());
    }

    @Test
    @DisplayName("유효한 닉네임으로 해당 친구가 있는 유저의 친구가 아닌 목록 조회")
    void findNonFriendsByValidFriendNickName() {
        //given
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();

        Member memberB = Member.builder()
                .email("emailB")
                .profile("profileB")
                .nickName("nickB")
                .role(Role.USER)
                .build();
        memberRepository.saveAll(List.of(memberA, memberB));
        friendshipRepository.save(Friendship.of(memberA, memberB));
        //when
        List<Member> nonfriendList = memberRepository.findNonFriendsByMemberNickName(memberB.getNickName(),
                memberA.getId());
        //then
        assertThat(nonfriendList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 닉네임으로 유저의 친구가 아닌 목록 조회")
    void findNonFriendsByInvalidMemberNickName() {
        //given
        String invalidNickName = "invalidNickName";
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();

        Member memberB = Member.builder()
                .email("emailB")
                .profile("profileB")
                .nickName("nickB")
                .role(Role.USER)
                .build();
        memberRepository.saveAll(List.of(memberA, memberB));
        //when
        List<Member> nonfriendList = memberRepository.findNonFriendsByMemberNickName(invalidNickName,
                memberA.getId());
        //then
        assertThat(nonfriendList.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("유효한 이메일으로 유저 조회")
    void findMemberByValidEmail() {
        //given
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();
        memberRepository.save(memberA);
        //when
        Optional<Member> memberByEmail = memberRepository.findMemberByEmail(memberA.getEmail());
        //then
        assertThat(memberByEmail.isPresent()).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 이메일으로 유저 조회")
    void findMemberByInvalidEmail() {
        //given
        String invalidEmail = "invalidEmail";
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();
        memberRepository.save(memberA);
        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> memberRepository.findMemberByEmail(invalidEmail)
                        .orElseThrow(IllegalArgumentException::new));
    }

    @Test
    @DisplayName("회원 id들이 담긴 리스트가 주어졌을 때 해당 회원들 조회")
    void findMembersByMemberIdList() {
        //given
        Member memberA = Member.builder()
                .email("emailA")
                .profile("profileA")
                .nickName("nickA")
                .role(Role.USER)
                .build();
        memberRepository.save(memberA);
        Member memberB = Member.builder()
                .email("emailB")
                .profile("profileB")
                .nickName("nickB")
                .role(Role.USER)
                .build();
        memberRepository.save(memberB);
        //when
        List<Member> members = memberRepository.findMembersByMemberIdList(List.of(memberA.getId(), memberB.getId()));
        //then
        assertThat(members.size()).isEqualTo(2);
    }
}