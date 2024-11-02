package com.stool.studentcooperationtools.domain.slide.repository;

import com.stool.studentcooperationtools.domain.slide.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Long> {

    @Query(value = "select s from Slide s join fetch s.script where s.presentation.id = :presentationId")
    List<Slide> findSlidesAndScriptsByPresentationId(@Param("presentationId") Long presentationId);

    List<Slide> findByPresentationId(Long presentationId);
}
