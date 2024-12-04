package com.stool.studentcooperationtools.domain.vote.respository;

import com.stool.studentcooperationtools.IntegrationTest;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.topic.repository.TopicRepository;
import com.stool.studentcooperationtools.domain.vote.Vote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class VoteRepositoryTest extends IntegrationTest {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @DisplayName("투표의 정보와 삭제할 사람의 id를 받아서 투표를 삭제한다.")
    @Test
    void deleteVoteByIdAndDeleterId(){
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
        topic.addVoteNum();
        topicRepository.save(topic);
        voteRepository.save(vote);
        //when
        voteRepository.deleteVoteByIdAndDeleterId(vote.getId(), member.getId());
        List<Vote> votes = voteRepository.findAll();
        //then
        assertThat(votes).hasSize(0);
    }

    @DisplayName("주제에 대한 투표를 전부 삭제한다.")
    @Test
    void deleteAllByTopicId(){
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
        topic.addVoteNum();
        topicRepository.save(topic);
        voteRepository.save(vote);
        //when
        voteRepository.deleteAllByTopicId(topic.getId());
        List<Vote> votes = voteRepository.findAll();
        //then
        assertThat(votes).isEmpty();
    }

    @DisplayName("주제의 id들을 받아 투표 중 주제의 id를 참조하고 있는 투표를 제거한다.")
    @Test
    void deleteAllByInTopicId(){
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

        Topic topic1 =Topic.builder()
                .topic("주제")
                .member(member)
                .room(room)
                .build();
        Topic topic2 =Topic.builder()
                .topic("주제")
                .member(member)
                .room(room)
                .build();
        Vote vote1 = Vote.builder()
                .voter(member)
                .topic(topic1)
                .build();
        Vote vote2 = Vote.builder()
                .voter(member)
                .topic(topic2)
                .build();
        topic1.addVoteNum();
        topic2.addVoteNum();
        topicRepository.saveAll(List.of(topic1,topic2));
        voteRepository.saveAll(List.of(vote1,vote2));
        //when
        voteRepository.deleteAllByInTopicId(List.of(topic1.getId(),topic2.getId()));
        List<Vote> votes = voteRepository.findAll();
        //then
        assertThat(votes).isEmpty();
    }
}