package com.pomodoro.service.repository;

import com.pomodoro.model.GroupInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, Integer> {



    GroupInvitation findGroupInvitationById(Integer groupId);

}
