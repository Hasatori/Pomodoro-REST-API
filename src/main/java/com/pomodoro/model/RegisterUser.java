package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RegisterUser {
    @NotBlank(message = "Name is mandatory")
    @Column(unique = true)
    private String username;

    private String firstName, lastName;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    @Column(unique = true)
    private String email;


    @Pattern(regexp = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,40})"
            , message = "Password must be between 8 to 40 characters long, must contain at least one digit, one upper case character and one lower case character")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
