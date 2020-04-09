package com.pomodoro.model.change;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.message.Message;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "MESSAGE_CHANGE")
@Getter
@Setter
public class MessageChange extends Change {

    private String oldValue;

    private String newValue;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE", nullable = false, insertable = false, updatable = false)
    private Message messageObject;

    @JsonIgnore
    private Integer message;
}
