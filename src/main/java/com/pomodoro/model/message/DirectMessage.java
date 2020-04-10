package com.pomodoro.model.message;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.todo.GroupToDo;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity(name = "DIRECT_MESSAGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class DirectMessage extends Message{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPIENT", insertable = false, updatable = false)
    private User recipient;

    @JsonIgnore
    @Column(name = "RECIPIENT")
    private Integer recipientId;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PARENT_ID", insertable = false, updatable = false)
    protected DirectMessage parent;

    @Nullable
    @Column(name = "PARENT_ID")
    protected Integer parentId;

}
