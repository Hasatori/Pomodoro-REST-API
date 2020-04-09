package com.pomodoro.model.change;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.todo.ToDo;
import com.pomodoro.model.todo.ToDoStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "TO_DO_CHANGE")
@Getter
@Setter
public class ToDoChange extends Change {

    private String oldName;

    private String newName;

    private String oldDescription;

    private String newDescription;
    @Enumerated(EnumType.STRING)
    private ToDoStatus oldStatus;
    @Enumerated(EnumType.STRING)
    private ToDoStatus newStatus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TO_DO", nullable = false, insertable = false, updatable = false)
    private ToDo todoObject;
    @JsonIgnore
    private Integer todo;
}
