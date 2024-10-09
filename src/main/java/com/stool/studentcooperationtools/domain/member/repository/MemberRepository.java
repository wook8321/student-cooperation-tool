package com.stool.studentcooperationtools.domain.member.repository;

import com.stool.studentcooperationtools.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select '*' from Member m join Friendship f on f.me.id = :memberId and f.friend.id = m.id order by m.nickName asc")
    List<Member> findFriendsByMemberId(@Param("memberId") Long memberId);

    @Query("select '*' from Member m join Friendship f on f.me.id = :memberId and f.friend.id = m.id where m.nickName like %:nickName% order by m.nickName asc")
    Optional<List<Member>> findFriendsByMemberNickName(@Param("nickName") String nickName);

    @Query("select '*' from Member m left join Friendship f on f.me.id = :memberId and f.friend.id = m.id where m.nickName like %:nickName% and f.me.id is null order by m.nickName asc")
    Optional<List<Member>> findNonFriendsByMemberNickName(@Param("nickName") String nickName);

    @Query("select '*' from Member m where m.email like %:findEmail%")
    Optional<Member> findMemberByEmail(@Param("findEmail") String findEmail);
}
