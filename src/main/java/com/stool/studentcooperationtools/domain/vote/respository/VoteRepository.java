package com.stool.studentcooperationtools.domain.vote.respository;

import com.stool.studentcooperationtools.domain.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long> {
}
