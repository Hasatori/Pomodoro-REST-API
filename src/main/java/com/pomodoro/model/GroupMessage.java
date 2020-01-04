package com.pomodoro.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "GROUP_MESSAGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GroupMessage {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String value;
    private Date timestamp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID",insertable = false,updatable = false)
    private User author;

    @JsonIgnore
    @Column(name = "AUTHOR_ID")
    private int authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID",insertable = false,updatable = false)
    private Group group ;

    @JsonIgnore
    @Column(name = "GROUP_ID")
    private int groupId;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "groupMessage")
    private List<UserGroupMessage> relatedGroupMessages;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "groupMessage")
    private List<GroupMessageChange> changes;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<UserGroupMessage> getRelatedGroupMessages() {
        return relatedGroupMessages;
    }

    public void setRelatedGroupMessages(List<UserGroupMessage> relatedGroupMessages) {
        this.relatedGroupMessages = relatedGroupMessages;
    }

    public List<GroupMessageChange> getChanges() {
        return changes;
    }

    public void setChanges(List<GroupMessageChange> changes) {
        this.changes = changes;
    }
}
