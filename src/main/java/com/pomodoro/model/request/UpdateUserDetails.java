package com.pomodoro.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UpdateUserDetails {
    @NotBlank(message = "Name is mandatory")
    @Column(unique=true)
    private String username;

    private String firstName, lastName;

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    @Column(unique=true)
    private String email;

}
