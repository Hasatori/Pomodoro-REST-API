package com.pomodoro.service.repository;

import com.pomodoro.model.message.DirectMessage;
import com.pomodoro.model.message.GroupMessage;
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
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Integer> {


}
