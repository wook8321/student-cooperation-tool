package com.stool.studentcooperationtools.domain.room.repository;

import com.stool.studentcooperationtools.domain.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select r from Room r join Participation p on p.member.id = :memberId and p.room.id = r.id order by r.id desc")
    Page<Room> findRoomsByMemberIdWithPage (@Param("memberId") Long memberId, Pageable pageable);

    @Query("select r from Room r join Participation p on p.member.id = :memberId and p.room.id = r.id where r.title like %:title% order by r.id desc")
    Page<Room> findRoomsByTitleWithPage(@Param("memberId") Long memberId, @Param("title") String title, Pageable pageable);

    @Query("select r from Room r join Participation p on p.member.id = :memberId where p.room.id = :roomId")
    Optional<Room> findByRoomId(@Param("memberId") Long memberId, @Param("roomId") Long roomId);

    @Query("select case when count(r) > 0 then true else false end from Room r join Participation p on p.member.id = :memberId where r.title like %:title%")
    Boolean existsByTitle(@Param("memberId") Long memberId, @Param("title") String title);
}
