package com.pomodoro.model.message;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.group.Group;
import com.pomodoro.model.todo.GroupToDo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity(name = "GROUP_MESSAGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class GroupMessage  extends Message{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
    private Group group;

    @JsonIgnore
    @Column(name = "GROUP_ID")
    private Integer groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PARENT_ID", insertable = false, updatable = false)
    protected GroupMessage parent;

    @Nullable
    @Column(name = "PARENT_ID")
    protected Integer parentId;

}
