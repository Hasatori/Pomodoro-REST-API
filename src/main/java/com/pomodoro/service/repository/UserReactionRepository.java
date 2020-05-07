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

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE REACTION SET READ_TIMESTAMP=:readTimestamp WHERE AUTHOR=:userId AND MESSAGE IN (:messageIds) ", nativeQuery = true)
    void markAllUserMessagesFromGroupAsRead(@Param("readTimestamp") LocalDateTime readTimestamp, @Param("userId") Integer userId, @Param("messageIds") List<Integer> messageIds);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE REACTION SET EMOJI=:emojiWHERE AUTHOR=:userId AND MESSAGE =:messageId", nativeQuery = true)
    void setReaction(@Param("emoji")String reaction,@Param("userId") Integer userId, @Param("messageId") Integer messageId);
}
