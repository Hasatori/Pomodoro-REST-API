package com.pomodoro.repository;

import com.pomodoro.model.Group;
import com.pomodoro.model.GroupInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, Integer> {



    GroupInvitation findGroupInvitationById(Integer groupId);

}
