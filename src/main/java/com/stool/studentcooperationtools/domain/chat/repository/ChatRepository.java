package com.stool.studentcooperationtools.domain.chat.repository;

import com.stool.studentcooperationtools.domain.chat.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ChatRepository extends JpaRepository<Chat,Long> {

    @Query("select c from Chat c join c.room r where r.id = :roomId order by c.id desc")
    Slice<Chat> findChatsByIdAndSlicingDESC(@Param("roomId")Long roomId, Pageable pageable);

    @Query("select c from Chat c join c.room r where r.id = :roomId and c.id < :lastMessageId order by c.id desc")
    Slice<Chat> findPrevChatsByIdAndSlicingDESC(@Param("roomId")Long roomId, Pageable pageable, @Param("lastMessageId") Long lastMessageId);

}
