package com.pomodoro.repository;

import com.pomodoro.model.GroupChange;
import com.pomodoro.model.GroupInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface GroupChangeRepository extends JpaRepository<GroupChange, Integer> {



    GroupChange findGroupChangeById(Integer groupId);

}
