package com.pomodoro.model.o2auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FacebookUser {

    @NotBlank(message = "Id is mandatory")
    private String id;
    @NotBlank(message = "Authentication token is mandatory")
    private String authToken;
    @NotBlank(message = "Email is mandatory")
    private String email;
    private String firstName;
    private String lastName;
    @NotBlank(message = "Name is mandatory")
    private String name;

}
