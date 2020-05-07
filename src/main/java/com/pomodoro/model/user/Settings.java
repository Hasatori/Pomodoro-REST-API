package com.pomodoro.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

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
    private Integer workDurationInSeconds;
    private Integer pauseDurationInSeconds;
    @Nullable
    private String phaseChangedSound;
    @Nullable
    private String workSound;
    @Nullable
    private String pauseSound;

}
