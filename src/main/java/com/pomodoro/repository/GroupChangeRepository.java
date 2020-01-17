package com.pomodoro.repository;

import com.pomodoro.model.GroupChange;
import com.pomodoro.model.GroupInvitation;
import com.pomodoro.model.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GroupChangeRepository extends JpaRepository<GroupChange, Integer> {



    GroupChange findGroupChangeById(Integer groupId);


    @Query(value = "select * from GROUP_CHANGE  where GROUP_ID=:groupId ORDER BY CHANGE_TIMESTAMP DESC limit :limit OFFSET :offset", nativeQuery = true)
    List<GroupChange> findLastChangesByGroupIdWithinLimitAndOffset(
            @Param("groupId") Integer groupId, @Param("limit") Integer limit, @Param("offset") Integer offset);

}
