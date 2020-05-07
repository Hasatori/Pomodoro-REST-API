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

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "RECIPIENT", insertable = false, updatable = false)
    private User recipient;

    @JsonIgnore
    @Column(name = "RECIPIENT")
    private Integer recipientId;

    @ManyToOne(fetch =  FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "REPLIED_MESSAGE_ID", insertable = false, updatable = false)
    protected DirectMessage repliedMessage;

    @JsonIgnore
    @Column(name = "REPLIED_MESSAGE_ID")
    protected Integer repliedMessageId;

}
