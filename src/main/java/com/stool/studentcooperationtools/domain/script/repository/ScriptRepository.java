package com.stool.studentcooperationtools.domain.script.repository;

import com.stool.studentcooperationtools.domain.script.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
}
