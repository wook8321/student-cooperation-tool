package com.stool.studentcooperationtools.domain.friendship.repository;

import com.stool.studentcooperationtools.domain.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("select '*' from Friendship f where f.me.id = :memberId and f.friend.id = :friendId")
    boolean existsByMemberIdAndFriendId(Long memberId, Long friendId);
}
