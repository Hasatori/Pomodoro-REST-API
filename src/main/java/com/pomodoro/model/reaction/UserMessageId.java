package com.pomodoro.model.reaction;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserMessageId implements Serializable {


    Integer authorId;
    Integer messageId;

}
