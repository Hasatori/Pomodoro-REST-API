package com.pomodoro.model.o2auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FacebookUser {

    @NotNull(message = "Id is mandatory")
    private Integer id;
    @NotBlank(message = "Authentication token is mandatory")
    private String authToken;
    @NotBlank(message = "Email is mandatory")
    private String email;
    private String firstName;
    private String lastName;
    @NotBlank(message = "Name is mandatory")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
