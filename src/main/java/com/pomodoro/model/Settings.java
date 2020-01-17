package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
@Entity(name = "SETTINGS")
@Getter
@Setter
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

}
