package com.stool.studentcooperationtools.domain.topic.repository;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.vote.Vote;
import com.stool.studentcooperationtools.domain.vote.respository.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class TopicRepositoryTest {

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    VoteRepository voteRepository;

    @DisplayName("방의 식별키로 해당 방의 주제들을 조회한다.")
    @Test
    void findAllByRoomId(){
        //given
        Room room = Room.builder()
                .password("password")
                .title("제목")
                .participationNum(0)
                .build();
        roomRepository.save(room);
        Member member = Member.builder()
                .email("email")
                .nickName("닉네임")
                .profile("profile")
                .role(Role.USER)
                .build();
        memberRepository.save(member);
        Topic topic =Topic.builder()
                .topic("주제")
                .member(member)
                .room(room)
                .build();
        Vote vote = Vote.builder()
                .voter(member)
                .topic(topic)
                .build();
        topic.addVote(vote);
        topicRepository.save(topic);
        voteRepository.save(vote);

        //when
        List<Topic> topics = topicRepository.findAllByRoomId(room.getId());
        //then
        assertThat(topics).hasSize(1);
        assertThat(topics.get(0).getRoom())
                .extracting("password","title","participationNum")
                .containsExactly(room.getPassword(),room.getTitle(),room.getParticipationNum());
        assertThat(topics.get(0).getMember())
                .extracting("email","nickName","profile")
                .containsExactly(member.getEmail(),member.getNickName(),member.getProfile());
        assertThat(topics.get(0).getVotes().get(0).getId()).isNotNull();
    }

    @DisplayName("방장 id를 받아서 주제를 제거한다.")
    @Test
    void deleteTopicByLeader(){
        //given
        Member leader = Member.builder()
                .email("방장이메일")
                .nickName("방장")
                .profile("방장프로필")
                .role(Role.USER)
                .build();
        memberRepository.save(leader);
        Room room = Room.builder()
                .password("password")
                .title("제목")
                .leader(leader)
                .participationNum(0)
                .build();

        Member owner = Member.builder()
                .email("팀원이메일")
                .nickName("팀원")
                .profile("팀원프로필")
                .role(Role.USER)
                .build();
        Topic topic =Topic.builder()
                .topic("주제")
                .member(owner)
                .room(room)
                .build();
        roomRepository.save(room);
        memberRepository.save(owner);
        topicRepository.save(topic);

        //when
        int updatedData = topicRepository.deleteTopicByLeaderOrOwner(topic.getId(), leader.getId());
        List<Topic> topics = topicRepository.findAll();
        //then
        assertThat(topics).hasSize(0);
        assertThat(updatedData).isEqualTo(1);
    }


    @DisplayName("주제의 주인 id를 받아서 주제를 제거한다.")
    @Test
    void deleteTopicByOwner(){
        //given
        Member leader = Member.builder()
                .email("방장이메일")
                .nickName("방장")
                .profile("방장프로필")
                .role(Role.USER)
                .build();
        memberRepository.save(leader);
        Room room = Room.builder()
                .password("password")
                .title("제목")
                .leader(leader)
                .participationNum(0)
                .build();

        Member owner = Member.builder()
                .email("팀원이메일")
                .nickName("팀원")
                .profile("팀원프로필")
                .role(Role.USER)
                .build();
        Topic topic =Topic.builder()
                .topic("주제")
                .member(owner)
                .room(room)
                .build();
        roomRepository.save(room);
        memberRepository.save(owner);
        topicRepository.save(topic);

        //when
        int updatedData = topicRepository.deleteTopicByLeaderOrOwner(topic.getId(), owner.getId());
        List<Topic> topics = topicRepository.findAll();
        //then
        assertThat(topics).hasSize(0);
        assertThat(updatedData).isEqualTo(1);
    }

    @DisplayName("주제의 주인,방장이 아닌 제 3자가 삭제할 경우 삭제하지 않는다.")
    @Test
    void deleteTopicByAnother(){
        //given
        Member leader = Member.builder()
                .email("방장이메일")
                .nickName("방장")
                .profile("방장프로필")
                .role(Role.USER)
                .build();
        memberRepository.save(leader);
        Room room = Room.builder()
                .password("password")
                .title("제목")
                .leader(leader)
                .participationNum(0)
                .build();

        Member owner = Member.builder()
                .email("팀원이메일")
                .nickName("팀원")
                .profile("팀원프로필")
                .role(Role.USER)
                .build();
        Topic topic =Topic.builder()
                .topic("주제")
                .member(owner)
                .room(room)
                .build();
        roomRepository.save(room);
        memberRepository.save(owner);
        topicRepository.save(topic);

        //when
        long invalidId = 2024L;
        int updatedData = topicRepository.deleteTopicByLeaderOrOwner(topic.getId(), invalidId);
        List<Topic> topics = topicRepository.findAll();
        //then
        assertThat(topics).hasSize(1);
        assertThat(updatedData).isEqualTo(0);
    }
}