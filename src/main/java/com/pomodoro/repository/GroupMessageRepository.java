package com.pomodoro.repository;

import com.pomodoro.model.Group;
import com.pomodoro.model.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {

    @Modifying
    @Query(value = "INSERT INTO GROUP_MESSAGE( VALUE, TIMESTAMP, AUTHOR_ID, GROUP_ID) VALUES (:value,:timestamp ,:authorId, :groupId)", nativeQuery = true)
    void insertGroupMessage(@Param("value") String value, @Param("timestamp") Date timestamp, @Param("authorId") Integer authorId, @Param("groupId") Integer groupId);


    @Query(value = "select * from GROUP_MESSAGE  where GROUP_ID=:groupId ORDER BY TIMESTAMP DESC limit :limit OFFSET :offset", nativeQuery = true)
    List<GroupMessage> findLastMessagesByGroupIdWithinLimitAndOffset(
            @Param("groupId") Integer groupId, @Param("limit") Integer limit, @Param("offset") Integer offset);
}
