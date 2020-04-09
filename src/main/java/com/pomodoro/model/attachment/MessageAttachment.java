package com.pomodoro.model.attachment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.message.Message;
import com.pomodoro.model.todo.ToDo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "MESSAGE_ATTACHMENT")
@Getter
@Setter
public class MessageAttachment extends Attachment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE",insertable = false,updatable = false)
    private Message message;

    @JsonIgnore
    @Column(name = "MESSAGE")
    private Integer messageId;
}
