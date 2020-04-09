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
    @Query(value="INSERT INTO SETTINGS(USER_ID,WORK_TIME,PAUSE_TIME,PHASE_CHANGED_SOUND,WORK_SOUND,PAUSE_SOUND) VALUES(:id,:workTime,:pauseTime,:phaseChangedSound,:workSound,:pauseSound)",nativeQuery = true)
    void insertNewSettings(@Param("id") Integer id,@Param("workTime") Integer workTime,@Param("pauseTime") Integer pauseTime,@Param("phaseChangedSound") String phaseChangedSound,@Param("workSound") String workSound,@Param("pauseSound") String pauseSound);

    @Modifying
    @Query("update SETTINGS s set s.workTime=?2, s.pauseTime = ?3, s.phaseChangedSound = ?4,s.workSound=?5,s.pauseSound=?6 where s.id = ?1")
    void updateUserSettings(Integer id, Integer workTime, Integer pauseTime, String phaseChangedSound, String workSound,String pauseSound);
}
