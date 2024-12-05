package com.stool.studentcooperationtools.domain.file.repository;

import com.stool.studentcooperationtools.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {

    @Modifying(flushAutomatically = true)
    @Query("delete File f " +
            "where f.id = :fileId and " +
            "(f.part.member.id =: deleterId or f.part.room.leader.id = :deleterId)")
    int deleteFileByIdAndLeaderOrOwner(@Param("deleterId") Long deleterId,@Param("fileId") Long fileId);

    @Modifying
    @Query("delete from File f where f.part.id in :partIds")
    void deleteAllByInPartId(@Param("partIds") List<Long> partIdsByRoomId);
}
