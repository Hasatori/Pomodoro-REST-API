package com.pomodoro.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GroupCreationRequest {

    @NotBlank(message = "Group name must be filled")
    private String groupName;
    @NotBlank(message = "Group layout image must be filled")
    private String layoutImage;

    private String description;

    @NotNull(message = "Is group public must be filled")
    private Boolean isPublic;
}
