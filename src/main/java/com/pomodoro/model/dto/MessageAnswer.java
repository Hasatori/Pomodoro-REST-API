package com.pomodoro.model.dto;

import com.pomodoro.model.message.Message;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageAnswer {

    private Message answeredMessage;
    private String answerValue;

}
