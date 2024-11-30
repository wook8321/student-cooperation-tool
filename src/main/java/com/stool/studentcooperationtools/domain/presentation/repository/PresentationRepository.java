package com.stool.studentcooperationtools.domain.presentation.repository;

import com.stool.studentcooperationtools.domain.presentation.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Long> {

    Optional<Presentation> findByRoomId(Long roomId);

    Boolean existsByRoomId(Long roomId);

    @Modifying
    @Query("delete from Presentation p where p.room.id =:roomId")
    void deleteByRoomId(@Param("roomId") Long roomId);

}