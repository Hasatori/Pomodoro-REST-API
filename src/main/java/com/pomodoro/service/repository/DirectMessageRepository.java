package com.pomodoro.service.repository;

import com.pomodoro.model.message.DirectMessage;
import com.pomodoro.model.message.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Integer> {

    @Modifying
    @Query(value = "SELECT * FROM DIRECT_MESSAGE dm LEFT JOIN REACTION r ON (dm.ID=r.MESSAGE ) WHERE ID=:userId and r.READ_TIMESTAMP IS NULL", nativeQuery = true)
    List<DirectMessage> findAllUnreadMessages(@Param("userId") Integer userId);

    @Query(value = "select * from DIRECT_MESSAGE  WHERE (AUTHOR_ID=:user1 AND RECIPIENT=:user2) OR (AUTHOR_ID=:user2 AND RECIPIENT=:user1) ORDER BY CREATION_TIMESTAMP DESC limit :limit OFFSET :offset", nativeQuery = true)
    List<DirectMessage> findLastMessagesByUserIdWithinLimitAndOffset(
            @Param("user1") Integer user1,@Param("user2") Integer user2, @Param("limit") Integer limit, @Param("offset")  Integer offset);


}
