package com.stool.studentcooperationtools.domain.vote.respository;

import com.stool.studentcooperationtools.domain.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote,Long> {

    @Modifying
    @Query(value = "delete from Vote v where v.voter.id = :deleterId and v.id = :voteId")
    Boolean deleteVoteByIdAndDeleterId(@Param("voteId") Long voteId, @Param("deleterId") Long deleterId);

}
