package com.pomodoro.service.repository;

import com.pomodoro.model.todo.UserToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserTodoRepository extends JpaRepository<UserToDo, Integer> {


    @Modifying
    @Query("delete from USER_TO_DO where ID in (:ids)")
    void deleteUserTodos(@Param("ids") List<Integer> ids);

    UserToDo findUserToDoById(Integer id);

}
