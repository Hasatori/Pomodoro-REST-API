package com.pomodoro.model.attachment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.User;
import com.pomodoro.model.todo.ToDo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "ATTACHMENT")
@Getter
@Setter
public class ToDoAttachment extends Attachment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TO_DO",insertable = false,updatable = false)
    private ToDo toDo;

    @JsonIgnore
    @Column(name = "TO_DO")
    private Integer todoId;
}
