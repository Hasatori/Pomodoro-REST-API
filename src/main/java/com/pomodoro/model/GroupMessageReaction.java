package com.pomodoro.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GroupMessageReaction {

    @NotNull(message = "Group message id must be filled")
    private Integer groupMessageId;
    @NotBlank(message = "Reaction must be filled")
    private String reaction;

}
