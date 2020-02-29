package com.pomodoro.service.repository;

import com.pomodoro.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Integer> {



    List<Group> findPomodoroGroupByName(String groupName);

    Group findPomodoroGroupById(Integer id);

    Group findPomodoroGroupByNameAndOwnerId(String groupName,Integer ownerId);

    @Modifying
    @Query(value = "INSERT INTO POMODORO_GROUP( NAME, OWNER_ID,IS_PUBLIC) VALUES (:name,:ownerId,:isPublic)",nativeQuery = true)
    void insertGroup(@Param("name") String name, @Param("ownerId") Integer ownerId, @Param("isPublic")boolean isPublic);


}
