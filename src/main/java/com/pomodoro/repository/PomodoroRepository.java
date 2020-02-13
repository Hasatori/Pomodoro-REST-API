package com.pomodoro.repository;

import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
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
    @Query(value = "INSERT INTO POMODORO(USER,CREATION_TIMESTAMP,  WORK_TIME, BREAK_TIME, INTERRUPTED) VALUES (:userId,:creationTimestamp,:workTime,:breakTime,:interrupted)",nativeQuery = true)
    void insertNewPomodoro(@Param("userId") Integer userId, @Param("creationTimestamp")Date creationTimestamp, @Param("workTime") int workTime, @Param("breakTime") int breakTime, @Param("interrupted") boolean interrupted);

    @Modifying
    @Query(value = "UPDATE POMODORO SET INTERRUPTED=TRUE where USER=?1 and CREATION_TIMESTAMP =?2",nativeQuery = true)
    void stopPomodoro(Integer userId,Date creationTimestamp);

}
