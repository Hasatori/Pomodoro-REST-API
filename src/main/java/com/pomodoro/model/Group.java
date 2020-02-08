package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "POMODORO_GROUP")
@Transactional
@Getter
@Setter
public class Group {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Boolean indicating if group is public must be filled")
    private Boolean isPublic;

    @NotNull(message = "Group created date must be filled")
    private Date created;

    @NotBlank(message = "Layout image must be filled")
    private String layoutImage;

    @Nullable
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID",insertable = false,updatable = false)
    private User owner;

    @JsonIgnore
    @Column(name = "OWNER_ID")
    private Integer ownerId;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "USER_GROUP",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    Set<User> users;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "group")
    private List<GroupMessage> groupMessages;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "group")
    private List<GroupInvitation> groupInvitations;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "group")
    private List<GroupChange> groupChanges;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "group")
    private List<GroupToDo> groupGroupToDos;

}
