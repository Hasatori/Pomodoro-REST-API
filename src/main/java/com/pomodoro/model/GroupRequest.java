package com.pomodoro.model;

import javax.validation.constraints.NotBlank;

public class GroupRequest {

    @NotBlank(message = "Username must be filled")
    private String username;
    @NotBlank(message = "Group name must be filled")
    private String groupName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
