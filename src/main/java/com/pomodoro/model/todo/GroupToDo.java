package com.pomodoro.model.todo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.group.Group;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "GROUP_TO_DO")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class GroupToDo extends ToDo {
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "USER_GROUP_TO_DO",
            joinColumns = @JoinColumn(name = "TO_DO_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    Set<User> assignedUsers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID",insertable = false,updatable = false)
    private Group group;

    @JsonIgnore
    @Column(name = "GROUP_ID")
    private Integer groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PARENT_TASK_ID", insertable = false, updatable = false)
    protected GroupToDo parentTask;

    @Nullable
    @Column(name = "PARENT_TASK_ID")
    protected Integer parentTaskId;
}
