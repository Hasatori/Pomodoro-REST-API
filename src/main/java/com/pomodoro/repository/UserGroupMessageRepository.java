package com.pomodoro.repository;

import com.pomodoro.model.Group;
import com.pomodoro.model.GroupMessage;
import com.pomodoro.model.UserGroupMessage;
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
public interface UserGroupMessageRepository extends JpaRepository<UserGroupMessage, Integer> {

    List<UserGroupMessage> findUserGroupMessagesByUserId(Integer userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE USER_GROUP_MESSAGE SET READ_TIMESTAMP=:readTimestamp WHERE USER_ID=:userId AND GROUP_MESSAGE_ID IN (:groupMessageIds) ", nativeQuery = true)
    void markAllUserMessagesFromGroupAsRead(@Param("readTimestamp")Date readTimestamp,@Param("userId") Integer userId, @Param("groupMessageIds") List<Integer> groupMessageIds);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE USER_GROUP_MESSAGE SET REACTION=:reaction WHERE USER_ID=:userId AND GROUP_MESSAGE_ID =:groupMessageId", nativeQuery = true)
    void setReaction(@Param("reaction")String reaction,@Param("userId") Integer userId, @Param("groupMessageId") Integer groupMessageId);
}
