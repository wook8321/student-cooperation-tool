package com.stool.studentcooperationtools.domain.topic.repository;

import com.stool.studentcooperationtools.domain.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic,Long> {
}
