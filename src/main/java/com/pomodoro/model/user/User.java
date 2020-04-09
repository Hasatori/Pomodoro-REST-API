package com.pomodoro.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomodoro.model.change.GroupChange;
import com.pomodoro.model.change.MessageChange;
import com.pomodoro.model.change.ToDoChange;
import com.pomodoro.model.group.Group;
import com.pomodoro.model.group.GroupInvitation;
import com.pomodoro.model.message.DirectMessage;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.reaction.DirectMessageReaction;
import com.pomodoro.model.reaction.GroupMessageReaction;
import com.pomodoro.model.todo.GroupToDo;
import com.pomodoro.model.todo.ToDo;
import com.pomodoro.model.todo.UserToDo;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.security.auth.Subject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "USER")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    @Column(unique = true)
    private String username;

    private String firstName, lastName;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is mandatory")
    @JsonIgnore
    private String password;

    @JsonIgnore
    private String token;
    @JsonIgnore
    private Boolean accountExpired;
    @JsonIgnore
    private Boolean locked;
    @JsonIgnore
    private Boolean credentialsExpired;
    @JsonIgnore
    private Boolean enabled;

    @JsonIgnore
    @Nullable
    private Integer facebookId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Settings settings;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private
    Set<Group> memberOfGroups;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Group> ownedGroups;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "userObject")
    private List<Pomodoro> pomodoros;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "invitedUser")
    private List<GroupInvitation> groupInvitations;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "recipient")
    private List<DirectMessage> directMessages;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "author")
    private List<GroupMessage> createdGroupMessages;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "author")
    private List<DirectMessage> createdDirectMessages;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "author")
    private List<DirectMessageReaction> directMessageReactions;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "author")
    private List<GroupMessageReaction> groupMessageReactions;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "changeAuthor")
    private List<ToDoChange> toDoChange;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "changeAuthor")
    private List<GroupChange> groupChanges;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "changeAuthor")
    private List<MessageChange> messageChange;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "author")
    private List<UserToDo> todos;

    public void setUsername(String userName) {
        this.username = userName;
    }
    @JsonIgnore
    @ManyToMany(mappedBy = "assignedUsers",cascade = CascadeType.REMOVE)

    private
    Set<GroupToDo> groupToDos;



    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public Boolean getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(Boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    @JsonIgnore
    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Pomodoro> getPomodoros() {
        return pomodoros;
    }

    public void setPomodoros(List<Pomodoro> pomodoros) {
        this.pomodoros = pomodoros;
    }

    public Set<Group> getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(Set<Group> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }

    public List<Group> getOwnedGroups() {
        return ownedGroups;
    }

    public void setOwnedGroups(List<Group> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Boolean getCredentialsExpired() {
        return credentialsExpired;
    }

    public Integer getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Integer facebookId) {
        this.facebookId = facebookId;
    }

    public List<DirectMessage> getCreatedDirectMessages() {
        return createdDirectMessages;
    }

    public void setCreatedDirectMessages(List<DirectMessage> createdDirectMessages) {
        this.createdDirectMessages = createdDirectMessages;
    }

    public List<GroupInvitation> getGroupInvitations() {
        return groupInvitations;
    }

    public void setGroupInvitations(List<GroupInvitation> groupInvitations) {
        this.groupInvitations = groupInvitations;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public List<DirectMessageReaction> getDirectMessageReactions() {
        return directMessageReactions;
    }

    public void setDirectMessageReactions(List<DirectMessageReaction> directMessageReactions) {
        this.directMessageReactions = directMessageReactions;
    }

    public List<UserToDo> getTodos() {
        return todos;
    }

    public void setTodos(List<UserToDo> todos) {
        this.todos = todos;
    }

    public Set<GroupToDo> getGroupToDos() {
        return groupToDos;
    }

    public void setGroupToDos(Set<GroupToDo> groupToDos) {
        this.groupToDos = groupToDos;
    }

    public List<DirectMessage> getDirectMessages() {
        return directMessages;
    }

    public void setDirectMessages(List<DirectMessage> directMessages) {
        this.directMessages = directMessages;
    }

    public List<GroupMessage> getCreatedGroupMessages() {
        return createdGroupMessages;
    }

    public void setCreatedGroupMessages(List<GroupMessage> createdGroupMessages) {
        this.createdGroupMessages = createdGroupMessages;
    }

    public List<GroupMessageReaction> getGroupMessageReactions() {
        return groupMessageReactions;
    }

    public void setGroupMessageReactions(List<GroupMessageReaction> groupMessageReactions) {
        this.groupMessageReactions = groupMessageReactions;
    }

    public List<GroupChange> getGroupChanges() {
        return groupChanges;
    }

    public void setGroupChanges(List<GroupChange> groupChanges) {
        this.groupChanges = groupChanges;
    }

    public List<ToDoChange> getToDoChange() {
        return toDoChange;
    }

    public void setToDoChange(List<ToDoChange> toDoChange) {
        this.toDoChange = toDoChange;
    }
}
