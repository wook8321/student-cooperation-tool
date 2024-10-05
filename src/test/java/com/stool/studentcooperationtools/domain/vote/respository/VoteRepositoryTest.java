package com.stool.studentcooperationtools.domain.vote.respository;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class VoteRepositoryTest {

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
        topic.addVote(vote);
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
        topic.addVote(vote);
        topicRepository.save(topic);
        voteRepository.save(vote);
        //when
        voteRepository.deleteAllByTopicId(topic.getId());
        List<Vote> votes = voteRepository.findAll();
        //then
        assertThat(votes).isEmpty();
    }
}