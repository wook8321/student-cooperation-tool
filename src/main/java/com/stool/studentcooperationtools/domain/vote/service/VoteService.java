package com.stool.studentcooperationtools.domain.vote.service;

import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.topic.Topic;
import com.stool.studentcooperationtools.domain.topic.repository.TopicRepository;
import com.stool.studentcooperationtools.domain.vote.Vote;
import com.stool.studentcooperationtools.domain.vote.respository.VoteRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import com.stool.studentcooperationtools.websocket.controller.vote.request.VoteUpdateWebSocketRequest;
import com.stool.studentcooperationtools.websocket.controller.vote.response.VoteUpdateWebSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;

    @Transactional
    public VoteUpdateWebSocketResponse updateVote(final VoteUpdateWebSocketRequest request, final SessionMember sessionMember) {
        Vote findVote = voteRepository.findVoteByMemberIdAndTopicId(sessionMember.getMemberSeq(), request.getTopicId());
        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new IllegalArgumentException("투표할 주제가 존재하지 않습니다."));
        if(findVote != null){
            //만약 투표를 했다면 투표를 취소 한다.
            topic.minusVoteNum();
            voteRepository.deleteById(findVote.getId());
            return VoteUpdateWebSocketResponse.of(topic);
        }
        //만약 투표를 안 했다면 투표를 등록 한다.
        Member member = memberRepository.findById(sessionMember.getMemberSeq())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        Vote vote = Vote.of(member, topic);
        voteRepository.save(vote);
        return VoteUpdateWebSocketResponse.of(topic);
    }

    @Transactional
    public Boolean deleteVote(final Long voteId,SessionMember member) {
        voteRepository.deleteVoteByIdAndDeleterId(voteId, member.getMemberSeq());
        return true;
    }
}
