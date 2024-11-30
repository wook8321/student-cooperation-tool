package com.stool.studentcooperationtools.domain.vote.service;

import com.stool.studentcooperationtools.IntegrationTest;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.topic.repository.TopicRepository;
import com.stool.studentcooperationtools.domain.vote.Vote;
import com.stool.studentcooperationtools.domain.vote.respository.VoteRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.vote.request.VoteUpdateWebSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.vote.response.VoteUpdateWebSocketResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class VoteServiceTest extends IntegrationTest {

    @Autowired
    VoteService voteService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    VoteRepository voteRepository;


    @DisplayName("주제에 투표할 때, 이미 투표하는 유저가 한번 더 투표할 경우 투표를 취소한다.")
    @Test
    void updateVoteWithSecondVote(){
        //given
        Member member = Member.builder()
                .nickName("닉네임")
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        Room room = Room.builder()
                .password("password")
                .title("제목")
                .participationNum(1)
                .build();

        roomRepository.save(room);

        int voteNum = 2;
        Topic topic = Topic.builder()
                .topic("주제")
                .room(room)
                .member(member)
                .voteNum(voteNum)
                .build();

        topicRepository.save(topic);

        Vote vote = Vote.builder()
                .voter(member)
                .topic(topic)
                .build();

        voteRepository.save(vote);

        SessionMember sessionMember = SessionMember.builder()
                .profile(member.getProfile())
                .memberSeq(member.getId())
                .nickName(member.getNickName())
                .build();

        VoteUpdateWebSocketRequest request = VoteUpdateWebSocketRequest.builder()
                .roomId(room.getId())
                .topicId(topic.getId())
                .build();

        //when
        VoteUpdateWebSocketResponse response = voteService.updateVote(request, sessionMember);
        //then
        assertThat(response).isNotNull()
                .extracting("topicId","voteNum")
                .containsExactlyInAnyOrder(topic.getId(),voteNum - 1);
    }


    @DisplayName("주제에 투표할 때, 투표하는 유저가 존재하지 않을 경우 에러가 발생한다")
    @Test
    void updateVoteWithNotExistMember(){
        //given
        Member member = Member.builder()
                .nickName("닉네임")
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        Room room = Room.builder()
                .password("password")
                .title("제목")
                .participationNum(1)
                .build();

        roomRepository.save(room);

        int voteNum = 2;
        Topic topic = Topic.builder()
                .topic("주제")
                .room(room)
                .member(member)
                .voteNum(voteNum)
                .build();

        topicRepository.save(topic);

        Long InvalidMemberId = 2024L;
        VoteUpdateWebSocketRequest request = VoteUpdateWebSocketRequest.builder()
                .roomId(room.getId())
                .topicId(topic.getId())
                .build();

        SessionMember sessionMember = SessionMember.builder()
                .profile("profile")
                .memberSeq(InvalidMemberId)
                .nickName("닉네임")
                .build();

        //when
        //then
        assertThatThrownBy(() ->voteService.updateVote(request,sessionMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 유저는 존재하지 않습니다.");
    }


    @DisplayName("주제에 투표할 때, 투표할 주제가 존재하지 않을 경우 에러가 발생한다")
    @Test
    void updateVoteWithNotExistTopic(){
        //given
        Member member = Member.builder()
                .nickName("닉네임")
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        SessionMember sessionMember = SessionMember.builder()
                .profile(member.getProfile())
                .memberSeq(member.getId())
                .nickName(member.getNickName())
                .build();

        VoteUpdateWebSocketRequest request = VoteUpdateWebSocketRequest.builder()
                .roomId(1L)
                .topicId(1L)
                .build();
        //when
        //then
        assertThatThrownBy(() ->voteService.updateVote(request, sessionMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("투표할 주제가 존재하지 않습니다.");
    }


    @DisplayName("주제 투표를 안했다면 투표를 생성해서 등록한다.")
    @Test
    void updateVote(){
        //given
        Member member = Member.builder()
                .nickName("닉네임")
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        Room room = Room.builder()
                .password("password")
                .title("제목")
                .participationNum(1)
                .build();

        roomRepository.save(room);

        int voteNum = 2;
        Topic topic = Topic.builder()
                .topic("주제")
                .room(room)
                .member(member)
                .voteNum(voteNum)
                .build();

        topicRepository.save(topic);

        SessionMember sessionMember = SessionMember.builder()
                .profile(member.getProfile())
                .memberSeq(member.getId())
                .nickName(member.getNickName())
                .build();

        VoteUpdateWebSocketRequest request = VoteUpdateWebSocketRequest.builder()
                .roomId(room.getId())
                .topicId(topic.getId())
                .build();

        //when
        VoteUpdateWebSocketResponse response = voteService.updateVote(request, sessionMember);
        //then
        assertThat(response).isNotNull()
                .extracting("topicId","voteNum")
                .containsExactlyInAnyOrder(topic.getId(),voteNum + 1);
    }
}