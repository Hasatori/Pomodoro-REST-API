package com.pomodoro.model.message;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.user.User;
import com.pomodoro.model.reaction.DirectMessageReaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "directMessage")
    private List<DirectMessageReaction> reactions;



}
