package com.pomodoro.model.dto;

import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsUserTyping {

    private User user;
    private Boolean isTyping;

}
