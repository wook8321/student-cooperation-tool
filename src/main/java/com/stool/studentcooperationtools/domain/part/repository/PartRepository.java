package com.stool.studentcooperationtools.domain.part.repository;

import com.stool.studentcooperationtools.domain.part.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartRepository extends JpaRepository<Part,Long> {

    @Query("select p from Part p join fetch p.room r join fetch p.member where r.id = :roomId")
    List<Part> findAllByRoomId(@Param("roomId") Long roomId);

    @Modifying
    @Query("delete Part p " +
            "where p.id = :partId and " +
            "(p.member.id = :deleterId or p.room.leader.id = :deleterId)")
    int deletePartByLeaderOrOwner(@Param("partId")Long partId,@Param("deleterId")Long deleterId);

    @Modifying
    @Query("delete from Part p where p.room.id =:roomId")
    void deleteByRoomId(@Param("roomId") Long roomId);

}
