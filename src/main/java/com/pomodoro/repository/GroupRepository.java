package com.pomodoro.repository;

import com.pomodoro.model.Group;
import com.pomodoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Integer> {



    Set<Group> findPomodoroGroupByName(String groupName);
    Group findPomodoroGroupByNameAndOwner(String groupName,Integer owner);
    @Modifying
    @Query(value = "INSERT INTO POMODORO_GROUP( NAME, OWNER,IS_PUBLIC) VALUES (:name,:owner,:isPublic)",nativeQuery = true)
    void insertGroup(@Param("name") String name, @Param("owner") Integer owner, @Param("isPublic")boolean isPublic);


}
