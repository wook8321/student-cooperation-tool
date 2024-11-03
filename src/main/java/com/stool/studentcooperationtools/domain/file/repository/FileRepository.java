package com.stool.studentcooperationtools.domain.file.repository;

import com.stool.studentcooperationtools.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,Long> {
}
