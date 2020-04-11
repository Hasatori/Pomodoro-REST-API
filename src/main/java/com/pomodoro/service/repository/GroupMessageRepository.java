package com.pomodoro.service.repository;

import com.pomodoro.model.message.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {


    GroupMessage findGroupMessageById(Integer id);

    @Modifying
    @Query(value = "INSERT INTO GROUP_MESSAGE( VALUE, CREATION_TIMESTAMP, AUTHOR_ID, GROUP_ID) VALUES (:value,:creationTimestamp ,:authorId, :groupId)", nativeQuery = true)
    void insertGroupMessage(@Param("value") String value, @Param("creationTimestamp") LocalDateTime timestamp, @Param("authorId") Integer authorId, @Param("groupId") Integer groupId);


    @Query(value = "select * from GROUP_MESSAGE  where GROUP_ID=:groupId ORDER BY CREATION_TIMESTAMP DESC limit :limit OFFSET :offset", nativeQuery = true)
    List<GroupMessage> findLastMessagesByGroupIdWithinLimitAndOffset(
            @Param("groupId") Integer groupId, @Param("limit") Integer limit, @Param("offset") Integer offset);


    @Modifying
    @Query(value = "SELECT * FROM GROUP_MESSAGE gm LEFT JOIN REACTION r ON (gm.ID=r.MESSAGE ) WHERE ID=:userId and GROUP_ID=:groupId and r.READ_TIMESTAMP IS NULL", nativeQuery = true)
    List<GroupMessage> findAllUnreadMessages(@Param("userId") Integer userId, @Param("groupId")Integer groupId);



    @Query(value = "SELECT * FROM GROUP_MESSAGE WHERE GROUP_ID=:groupId ORDER BY CREATION_TIMESTAMP DESC LIMIT 1 ", nativeQuery = true)
    GroupMessage findNewestGroupMessageByGroupId( @Param("groupId")Integer groupId);


}
