package com.pomodoro.service.repository;

import com.pomodoro.model.GroupToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional
public interface GroupTodoRepository extends JpaRepository<GroupToDo, Integer> {


    Set<GroupToDo> findGroupToDoByGroupId(Integer groupId);

    GroupToDo findGroupToDoById(Integer id);

    @Modifying
    @Query("delete from GROUP_TO_DO where ID in (:ids)")
    void deleteGroupTodos(@Param("ids") List<Integer> ids);
}
