package com.pomodoro.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GroupUserRequest {

    @NotBlank(message = "Username must be filled")
    private String username;
    @NotBlank(message = "Group name must be filled")
    private String groupName;

}
