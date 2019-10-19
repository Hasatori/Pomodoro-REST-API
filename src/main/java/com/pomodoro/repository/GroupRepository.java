package com.pomodoro.repository;

import com.pomodoro.model.Group;
import com.pomodoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Integer> {



    Set<Group> findPomodoroGroupByName(String groupName);


}
