package com.pomodoro.model.reaction;

import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.message.Message;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserMessageId implements Serializable {


    User author;
    Message message;

}
