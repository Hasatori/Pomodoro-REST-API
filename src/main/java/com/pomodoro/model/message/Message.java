package com.pomodoro.model.message;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.attachment.MessageAttachment;
import com.pomodoro.model.attachment.ToDoAttachment;
import com.pomodoro.model.reaction.UserReaction;
import com.pomodoro.model.user.User;
import com.pomodoro.model.change.MessageChange;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "MESSAGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "message_generator")
    @SequenceGenerator(name="message_generator", sequenceName = "message_seq")
    protected Integer id;

    @Lob
    protected String value;


    protected LocalDateTime creationTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID", insertable = false, updatable = false)
    protected User author;

    @JsonIgnore
    @Column(name = "AUTHOR_ID")
    protected Integer authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ANSWERED_MESSAGE", insertable = false, updatable = false)
    protected Message answeredMessage;

    @Nullable
    @Column(name = "ANSWERED_MESSAGE")
    protected Integer answeredMessageId;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "messageObject")
    protected List<MessageChange> changes;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "message")
    private List<UserReaction> reactions;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "message")
    private List<MessageAttachment> attachments;
}
