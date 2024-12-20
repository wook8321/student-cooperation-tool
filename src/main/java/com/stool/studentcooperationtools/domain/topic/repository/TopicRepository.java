package com.stool.studentcooperationtools.domain.topic.repository;

import com.stool.studentcooperationtools.domain.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Long> {
    @Query("select t from Topic t join t.member join t.room " +
            "where t.room.id = :roomId " +
            "order by t.createdTime desc")
    List<Topic> findAllByRoomId(@Param("roomId") Long roomId);

    @Modifying
    @Query(value = "delete from Topic t " +
            "where t.id = :topicId and " +
            "(t.member.id = :deleterId or t.room.leader.id = :deleterId)")
    int deleteTopicByLeaderOrOwner(@Param("topicId") Long topicId, @Param("deleterId") Long deleterId);

    @Modifying
    @Query("delete from Topic t where t.room.id =:roomId")
    void deleteByRoomId(@Param("roomId") Long roomId);

    @Query("select t.id from Topic t where t.room.id = :roomId")
    List<Long> findTopicIdByRoomId(@Param("roomId") Long roomId);

}
