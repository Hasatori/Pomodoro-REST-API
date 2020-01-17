package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "POMODORO")
@Transactional
@Getter
@Setter
public class Pomodoro {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER", nullable = false,insertable = false,updatable = false)
    private User userObject;
    @JsonIgnore
    private int user;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;
    private int workTime;
    private int breakTime;
    private boolean interrupted;

}
