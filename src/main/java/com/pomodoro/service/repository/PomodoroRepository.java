package com.pomodoro.service.repository;

import com.pomodoro.model.user.Pomodoro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
@Transactional
public interface PomodoroRepository  extends JpaRepository<Pomodoro, Integer> {


    @Modifying
    @Query(value = "UPDATE POMODORO SET INTERRUPTED=TRUE where USER=?1 and CREATION_TIMESTAMP =?2",nativeQuery = true)
    void stopPomodoro(Integer userId,LocalDateTime creationTimestamp);

}
