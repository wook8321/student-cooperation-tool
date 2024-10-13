package com.stool.studentcooperationtools.domain.topic.repository;

import com.stool.studentcooperationtools.domain.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Long> {
    @Query("select t from Topic t join t.member join t.room " +
            "where t.room.id = :roomId " +
            "order by t.createdTime desc")
    List<Topic> findAllByRoomId(@Param("roomId") Long roomId);
}
