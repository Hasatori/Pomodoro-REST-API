package com.pomodoro.repository;

import com.pomodoro.model.Settings;
import com.pomodoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SettingsRepository extends JpaRepository<Settings, Integer> {

    @Modifying
    @Query("update SETTINGS s set s.workTime=?2, s.pauseTime = ?3, s.phaseChangedSound = ?4,s.workSound=?5,s.pauseSound=?6 where s.user_id = ?1")
    void updateUserSettings(Integer id, Integer workTime, Integer pauseTime, String phaseChangedSound, String workSound,String pauseSound);
}
