package com.stool.studentcooperationtools.domain.slide.repository;

import com.stool.studentcooperationtools.domain.slide.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Long> {

    List<Slide> findByPresentationId(Long presentationId);
}
