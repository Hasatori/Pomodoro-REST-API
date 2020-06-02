package com.pomodoro.service.repository;


import com.pomodoro.model.reaction.UserReaction;
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
public interface UserReactionRepository extends JpaRepository<UserReaction, Integer> {

    List<UserReaction> findUserReactionByAuthor(Integer author);

    UserReaction findUserReactionByAuthorIdAndMessageId(Integer authorId, Integer messageId);


    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE REACTION SET READ_TIMESTAMP=:readTimestamp WHERE READ_TIMESTAMP IS NULL AND AUTHOR=:userId AND MESSAGE IN (:messageIds) ", nativeQuery = true)
    void markAllMessagesAsRead(@Param("readTimestamp") LocalDateTime readTimestamp, @Param("userId") Integer userId, @Param("messageIds") List<Integer> messageIds);


    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE REACTION SET EMOJI=:emoji WHERE AUTHOR=:userId AND MESSAGE =:messageId", nativeQuery = true)
    void setReaction(@Param("emoji") String reaction, @Param("userId") Integer userId, @Param("messageId") Integer messageId);
}
