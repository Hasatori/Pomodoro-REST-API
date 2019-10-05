package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.objects.annotations.Property;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "POMODORO")
@Transactional
public class Pomodoro {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER", nullable = false,insertable = false,updatable = false)
    private User userObject;

    private int user;
    private Date date;
    private int workTimeRemaining;
    private int breakTimeRemaining;
    private boolean interrupted;
    private int numberOfInterruptions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserObject() {
        return userObject;
    }

    public void setUserObject(User userObject) {
        this.userObject = userObject;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWorkTimeRemaining() {
        return workTimeRemaining;
    }

    public void setWorkTimeRemaining(int workTimeRemaining) {
        this.workTimeRemaining = workTimeRemaining;
    }

    public int getBreakTimeRemaining() {
        return breakTimeRemaining;
    }

    public void setBreakTimeRemaining(int breakTimeRemaining) {
        this.breakTimeRemaining = breakTimeRemaining;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public int getNumberOfInterruptions() {
        return numberOfInterruptions;
    }

    public void setNumberOfInterruptions(int numberOfInterruptions) {
        this.numberOfInterruptions = numberOfInterruptions;
    }
}
