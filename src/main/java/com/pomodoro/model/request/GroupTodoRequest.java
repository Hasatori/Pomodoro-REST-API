package com.pomodoro.model.request;

import com.pomodoro.model.group.Group;
import com.pomodoro.model.todo.GroupToDo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class GroupTodoRequest {


    @NotNull(message = "Group must be filled")
    private Group group;

    @NotNull(message = "List of todos must be filled")
    private List<GroupToDo> toDoList;

}
