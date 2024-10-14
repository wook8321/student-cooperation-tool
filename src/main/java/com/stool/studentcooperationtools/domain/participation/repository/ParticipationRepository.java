package com.stool.studentcooperationtools.domain.participation.repository;

import com.stool.studentcooperationtools.domain.participation.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Boolean existsByMemberIdAndRoomId(Long memberId, Long roomId);
}
