package com.pomodoro.service.repository;

import com.pomodoro.model.user.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SettingsRepository extends JpaRepository<Settings, Integer> {

    @Modifying
    @Query(value="INSERT INTO SETTINGS(USER_ID,WORK_DURATION_IN_SECONDS,PAUSE_DURATION_IN_SECONDS,PHASE_CHANGED_SOUND,WORK_SOUND,PAUSE_SOUND) VALUES(:id,:workDurationInSeconds,:pauseDurationInSeconds,:phaseChangedSound,:workSound,:pauseSound)",nativeQuery = true)
    void insertNewSettings(@Param("id") Integer id,@Param("workDurationInSeconds") Integer workTime,@Param("pauseDurationInSeconds") Integer pauseTime,@Param("phaseChangedSound") String phaseChangedSound,@Param("workSound") String workSound,@Param("pauseSound") String pauseSound);

}
