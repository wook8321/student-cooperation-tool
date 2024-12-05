package com.stool.studentcooperationtools.domain.room.service;

import com.stool.studentcooperationtools.domain.chat.repository.ChatRepository;
import com.stool.studentcooperationtools.domain.file.repository.FileRepository;
import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.repository.MemberRepository;
import com.stool.studentcooperationtools.domain.part.repository.PartRepository;
import com.stool.studentcooperationtools.domain.participation.Participation;
import com.stool.studentcooperationtools.domain.participation.repository.ParticipationRepository;
import com.stool.studentcooperationtools.domain.presentation.repository.PresentationRepository;
import com.stool.studentcooperationtools.domain.review.repository.ReviewRepository;
import com.stool.studentcooperationtools.domain.room.Room;
import com.stool.studentcooperationtools.domain.room.controller.request.RoomRemoveRequest;
import com.stool.studentcooperationtools.domain.room.repository.RoomRepository;
import com.stool.studentcooperationtools.domain.script.repository.ScriptRepository;
import com.stool.studentcooperationtools.domain.slide.repository.SlideRepository;
import com.stool.studentcooperationtools.domain.topic.repository.TopicRepository;
import com.stool.studentcooperationtools.domain.vote.respository.VoteRepository;
import com.stool.studentcooperationtools.security.oauth2.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoomDeleteService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final ParticipationRepository participationRepository;
    private final SlideRepository slideRepository;
    private final ChatRepository chatRepository;
    private final PartRepository partRepository;
    private final PresentationRepository presentationRepository;
    private final VoteRepository voteRepository;
    private final ScriptRepository scriptRepository;
    private final ReviewRepository reviewRepository;
    private final FileRepository fileRepository;

    @Transactional
    public Boolean removeRoom(SessionMember member, final RoomRemoveRequest request) {
        Room room = roomRepository.findRoomWithPLock(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("방 id 오류"));
        if(Objects.equals(member.getMemberSeq(), room.getLeader().getId())){
            removePartIn(room);
            chatRepository.deleteByRoomId(room.getId());
            removeTopicIn(room);
            removePptIn(room);
            participationRepository.deleteByRoomId(room.getId());
            roomRepository.deleteById(room.getId());
        }
        else{
            Member teammate = memberRepository.findById(member.getMemberSeq())
                    .orElseThrow(() -> new IllegalArgumentException("유저 정보가 올바르지 않습니다"));
            if(participationRepository.existsByMemberIdAndRoomId(teammate.getId(), room.getId())){
                delParticipation(member, room);
                participationRepository.deleteByMemberIdAndRoomId(teammate.getId(), room.getId());
            }
            else{
                throw new IllegalArgumentException("참여 정보가 없는 유저입니다");
            }

        }
        return true;
    }

    private void removePartIn(final Room room) {
        List<Long> partIdsByRoomId = partRepository.findPartIdsByRoomId(room.getId());
        fileRepository.deleteAllByInPartId(partIdsByRoomId);
        reviewRepository.deleteAllByInPartId(partIdsByRoomId);
        partRepository.deleteByRoomId(room.getId());

    }

    private void removePptIn(final Room room) {
        Long presentationId = presentationRepository.findPresentationIdByRoomId(room.getId());
        slideRepository.deleteByPresentationId(presentationId);
        scriptRepository.deleteByPresentationId(presentationId);
        presentationRepository.deleteByRoomId(room.getId());
    }

    private void removeTopicIn(final Room room) {
        List<Long> topicIds = topicRepository.findTopicIdByRoomId(room.getId());
        voteRepository.deleteAllByInTopicId(topicIds);
        if(room.getMainTopic() != null){
            room.updateTopic(null);
        }
        topicRepository.deleteByRoomId(room.getId());
    }

    private void delParticipation(SessionMember member, Room room){
        Member user = memberRepository.findById(member.getMemberSeq())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 올바르지 않습니다"));
        Participation participation = participationRepository.findByMemberIdAndRoomId(user.getId(), room.getId());
        room.deleteParticipation(participation);
    }

}
