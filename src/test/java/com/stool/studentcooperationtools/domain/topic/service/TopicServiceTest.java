package com.stool.studentcooperationtools.domain.topic.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.topic.repository.TopicRepository;
import com.stool.studentcooperationtools.websocket.controller.topic.request.TopicAddSocketRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TopicServiceTest {

    @Autowired
    TopicService topicService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    TopicRepository topicRepository;

    @BeforeEach
    void tearUp(){
        topicRepository.deleteAll();
        roomRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("주제를 등록할 때, 주제를 등록하는 유저가 조회가 안될 경우 에러가 발생한다.")
    @Test
    void addTopicWithNotExistMember(){
        //given
        TopicAddSocketRequest request = TopicAddSocketRequest.builder()
                .roomId(1L)
                .topic("주제 제목")
                .build();
        Long InvalidMemberId = 1L;
        //when
        //then
        assertThatThrownBy(() -> topicService.addTopic(request,InvalidMemberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 유저는 존재하지 않습니다");
    }


    @DisplayName("주제를 등록할 때, 주제를 등록할 방이 조회가 안될 경우 에러가 발생한다.")
    @Test
    void addTopicWithNotExistRoom(){
        //given
        Member member = Member.builder()
                .email("email")
                .nickName("nickname")
                .profile("profile")
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        TopicAddSocketRequest request = TopicAddSocketRequest.builder()
                .roomId(1L)
                .topic("주제 제목")
                .build();
        //when
        //then
        assertThatThrownBy(() -> topicService.addTopic(request, member.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 방은 존재하지 않습니다.");
    }

    @DisplayName("주제와 방 정보를 받아서 주제를 등록한다.")
    @Test
    void addTopic() {
        //given
        Member member = Member.builder()
                .email("email")
                .nickName("nickname")
                .profile("profile")
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        Room room = Room.builder()
                .participationNum(0)
                .title("방 제목")
                .password("password")
                .build();

        roomRepository.save(room);


        TopicAddSocketRequest request = TopicAddSocketRequest.builder()
                .roomId(room.getId())
                .topic("주제 제목")
                .build();
        //when
        topicService.addTopic(request, member.getId());
        List<Topic> topics = topicRepository.findAll();
        //then
        assertThat(topics).hasSize(1)
                .extracting("topic")
                .containsExactlyInAnyOrder(request.getTopic());
    }
}