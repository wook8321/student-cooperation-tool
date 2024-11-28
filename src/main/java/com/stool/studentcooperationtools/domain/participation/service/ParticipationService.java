package com.stool.studentcooperationtools.domain.participation.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.participation.controller.response.ParticipationViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationService {

    private final MemberRepository memberRepository;

    public ParticipationViewResponse getParticipationIn(final Long roomId) {
        List<Member> members = memberRepository.findAllByRoomId(roomId);
        return ParticipationViewResponse.of(members);
    }
}
