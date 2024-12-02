package com.stool.studentcooperationtools.domain.slide.repository;

import com.stool.studentcooperationtools.domain.slide.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Long> {

    @Query(value = "select s from Slide s join fetch s.script where s.presentation.id = :presentationId")
    List<Slide> findSlidesAndScriptsByPresentationId(@Param("presentationId") Long presentationId);

    @Modifying
    @Transactional
    @Query(value = "delete from Slide s where s.presentation.id = :presentationId")
    void deleteByPresentationId(@Param("presentationId") Long presentationId);


}
