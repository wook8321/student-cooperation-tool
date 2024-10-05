package com.stool.studentcooperationtools.domain.friendship.repository;

import com.stool.studentcooperationtools.domain.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

}
