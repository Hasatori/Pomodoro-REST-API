package com.pomodoro.model.user;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "USER_REGISTRATION")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class UserRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;


    private LocalDateTime creationTimestamp;

    private LocalDateTime maturityDate;

    private String token;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER", insertable = false, updatable = false)
    protected User user;

    @Column(name = "USER")
    protected Integer userId;
}
