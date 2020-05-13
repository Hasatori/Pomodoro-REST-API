package com.pomodoro.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DirectMessageReaction {

    @NotBlank(message = "Emoji must e filled")
    private String emoji ;
    @NotNull(message = "Message id must be filled")
    private Integer messageId;
}
