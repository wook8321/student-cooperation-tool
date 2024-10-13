package com.stool.studentcooperationtools.domain.vote.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.topic.repository.TopicRepository;
import com.stool.studentcooperationtools.domain.vote.Vote;
import com.stool.studentcooperationtools.domain.vote.respository.VoteRepository;
import com.stool.studentcooperationtools.websocket.controller.vote.request.VoteAddWebSocketRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class VoteServiceTest {

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

    @DisplayName("주제에 투표할 때, 투표하는 유저가 존재하지 않을 경우 에러가 발생한다")
    @Test
    void addVoteWithNotExistMember(){
        //given
        Long InvalidMemberId = 1L;
        VoteAddWebSocketRequest request = VoteAddWebSocketRequest.builder()
                .roomId(1L)
                .topicId(1L)
                .build();
        //when
        //then
        assertThatThrownBy(() ->voteService.addVote(request,InvalidMemberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 유저는 존재하지 않습니다.");
    }


    @DisplayName("주제에 투표할 때, 투표할 주제가 존재하지 않을 경우 에러가 발생한다")
    @Test
    void addVoteWithNotExistTopic(){
        //given
        Member member = Member.builder()
                .nickName("닉네임")
                .email("email")
                .profile("profile")
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        VoteAddWebSocketRequest request = VoteAddWebSocketRequest.builder()
                .roomId(1L)
                .topicId(1L)
                .build();
        //when
        //then
        assertThatThrownBy(() ->voteService.addVote(request, member.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("투표할 주제가 존재하지 않습니다.");
    }


    @DisplayName("주제에 추가할 투표를 등록한다.")
    @Test
    void addVote(){
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
                .participationNum(0)
                .build();

        roomRepository.save(room);

        Topic topic = Topic.builder()
                .topic("주제")
                .room(room)
                .member(member)
                .build();

        topicRepository.save(topic);

        VoteAddWebSocketRequest request = VoteAddWebSocketRequest.builder()
                .roomId(room.getId())
                .topicId(topic.getId())
                .build();
        //when
        voteService.addVote(request, member.getId());
        List<Vote> votes = voteRepository.findAll();
        //then
        assertThat(votes).hasSize(1);
    }
}