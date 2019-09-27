package com.pomodoro.model;


import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "USER")
@Transactional
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "userObject")
    private Set<Pomodoro> pomodoros;
    private String firstName, lastName, password, email, token;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Pomodoro> getPomodoros() {
        return pomodoros;
    }

    public void setPomodoros(Set<Pomodoro> pomodoros) {
        this.pomodoros = pomodoros;
    }
}
