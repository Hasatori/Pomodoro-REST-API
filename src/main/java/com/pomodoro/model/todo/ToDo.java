package com.pomodoro.model.todo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.user.Pomodoro;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "TO_DO")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ToDo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ToDoStatus status;

    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PARENT_ID", insertable = false, updatable = false)
    private UserToDo parent;

    @Nullable
    @Column(name = "PARENT_ID")
    private Integer parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID", insertable = false, updatable = false)
    private User author;

    @Column(name = "AUTHOR_ID")
    private Integer authorId;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "TO_DO_FINISHED_POMODOROS",
            joinColumns = @JoinColumn(name = "TO_DO_ID"),
            inverseJoinColumns = @JoinColumn(name = "POMODORO_ID"))
    Set<Pomodoro> pomodoros;
}
