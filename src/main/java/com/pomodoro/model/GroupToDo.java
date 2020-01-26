package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "GROUP_TO_DO")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class GroupToDo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
    private Group group;

    @Column(name = "GROUP_ID")
    private Integer groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PARENT_ID", insertable = false, updatable = false)
    private GroupToDo parent;

    @Nullable
    @Column(name = "PARENT_ID")
    private Integer parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID",insertable = false,updatable = false)
    private User author;

    @Column(name = "AUTHOR_ID")
    private Integer authorId;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "USER_TO_DO",
            joinColumns = @JoinColumn(name = "TO_DO_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    Set<User> assignedUsers;


    private String status;

    private Date deadline;

    private String description;
}
