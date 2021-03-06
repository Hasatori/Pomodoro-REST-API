package com.pomodoro.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.group.Group;
import com.pomodoro.model.todo.ToDo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "POMODORO")
@Transactional
@Getter
@Setter
public class Pomodoro {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pomodoro_generator")
    @SequenceGenerator(name="pomodoro_generator", sequenceName = "pomodoro_seq")
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER", nullable = false,insertable = false,updatable = false)
    private User userObject;
    @JsonIgnore
    private Integer user;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;
    private Integer workDurationInSeconds;
    private Integer pauseDurationInSeconds;
    private boolean interrupted;

    private String pauseSound;
    private String workSound;
    private String phaseChangedSound;

    @JsonIgnore
    @ManyToMany(mappedBy = "finishedPomodoros")
    private
    Set<ToDo> finishedToDos;

}
