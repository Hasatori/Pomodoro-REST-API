package com.pomodoro.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GroupDataRequest {


    @NotBlank(message = "Group name must be filled")
    private String groupName;
    @NotNull(message = "From must be filled")
    private Integer start;
    @NotNull(message = "To must be filled")
    private Integer stop;

}
