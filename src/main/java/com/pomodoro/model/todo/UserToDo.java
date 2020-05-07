package com.pomodoro.model.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity(name = "USER_TO_DO")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class UserToDo extends ToDo {

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PARENT_TASK_ID", insertable = false, updatable = false)
    protected UserToDo parentTask;

    @Nullable
    @Column(name = "PARENT_TASK_ID")
    protected Integer parentTaskId;
}
