package com.stool.studentcooperationtools.domain.topic.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.topic.controller.response.TopicFindResponse;
import com.stool.studentcooperationtools.domain.topic.repository.TopicRepository;
import com.stool.studentcooperationtools.websocket.controller.topic.request.TopicAddSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.topic.request.TopicDeleteSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.topic.response.TopicAddSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TopicService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final TopicRepository topicRepository;

    public TopicFindResponse findTopics(final Long roomId) {
        return null;
    }

    @Transactional
    public TopicAddSocketResponse addTopic(final TopicAddSocketRequest request, final Long memberSeq) {
        Member member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다"));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("해당 방은 존재하지 않습니다."));
        Topic topic = Topic.builder()
                .topic(request.getTopic())
                .member(member)
                .room(room)
                .build();
        topicRepository.save(topic);
        return TopicAddSocketResponse.of(topic);
    }

    @Transactional
    public Boolean deleteTopic(final TopicDeleteSocketRequest request) {
        topicRepository.deleteById(request.getTopicId());
        return true;
    }
}
