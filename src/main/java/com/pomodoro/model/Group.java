package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "POMODORO_GROUP")
@Transactional
public class Group {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String name;
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID",insertable = false,updatable = false)
    private User owner;

    @JsonIgnore
    @Column(name = "OWNER_ID")
    private int ownerId;

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

    public Integer getId() {
        return id;
    }

    public List<GroupMessage> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(List<GroupMessage> groupMessages) {
        this.groupMessages = groupMessages;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
