package com.stool.studentcooperationtools.domain.vote.respository;

import com.stool.studentcooperationtools.domain.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote,Long> {

    @Modifying
    @Query(value = "delete from Vote v where v.voter.id = :deleterId and v.id = :voteId")
    void deleteVoteByIdAndDeleterId(@Param("voteId") Long voteId, @Param("deleterId") Long deleterId);

    @Modifying
    @Query(value = "delete from Vote v where v.topic.id = :topicId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);

    @Query(value = "select v from Vote v where v.voter.id = :memberId and v.topic.id = :topicId")
    Vote findVoteByMemberIdAndTopicId(@Param("memberId") Long memberId, @Param("topicId") Long topicId);
}
