package com.pomodoro.model.todo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.attachment.Attachment;
import com.pomodoro.model.attachment.ToDoAttachment;
import com.pomodoro.model.change.ToDoChange;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.user.Pomodoro;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "TO_DO")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "todo_generator")
    @SequenceGenerator(name="todo_generator", sequenceName = "todo_seq")
    protected Integer id;

    protected String name;
    protected String description;

    @Enumerated(EnumType.STRING)
    protected ToDoStatus status;

    protected LocalDateTime deadline;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID", insertable = false, updatable = false)
    protected User author;

    @Column(name = "AUTHOR_ID")
    protected Integer authorId;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "TO_DO_FINISHED_POMODOROS",
            joinColumns = @JoinColumn(name = "TO_DO_ID"),
            inverseJoinColumns = @JoinColumn(name = "POMODORO_ID"))
    Set<Pomodoro> finishedPomodoros;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "toDo")
    private List<ToDoChange> toDoChanges;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "toDo")
    private List<ToDoAttachment> attachments;

}
