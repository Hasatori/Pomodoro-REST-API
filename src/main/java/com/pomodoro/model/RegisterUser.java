package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Getter
@Setter
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

}
