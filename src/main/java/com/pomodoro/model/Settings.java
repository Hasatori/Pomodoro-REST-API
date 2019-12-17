package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
@Entity(name = "SETTINGS")
public class Settings {
    @JsonIgnore
    @Id
    @Column(name = "USER_ID")
    private Integer id;
    @JsonIgnore
    @OneToOne
    @MapsId
    private User user;
    private Integer workTime;
    private Integer pauseTime;
    @Nullable
    private String phaseChangedSound;
    @Nullable
    private String workSound;
    @Nullable
    private String pauseSound;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
    }

    public Integer getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(Integer pauseTime) {
        this.pauseTime = pauseTime;
    }

    public String getPhaseChangedSound() {
        return phaseChangedSound;
    }

    public void setPhaseChangedSound(String phaseChangedSound) {
        this.phaseChangedSound = phaseChangedSound;
    }

    public String getWorkSound() {
        return workSound;
    }

    public void setWorkSound(String workSound) {
        this.workSound = workSound;
    }

    public String getPauseSound() {
        return pauseSound;
    }

    public void setPauseSound(String pauseSound) {
        this.pauseSound = pauseSound;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
