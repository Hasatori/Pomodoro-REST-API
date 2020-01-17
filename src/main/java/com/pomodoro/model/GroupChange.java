package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "GROUP_CHANGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GroupChange {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String changeDescription;
    private Date changeTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
    private Group group;

    @JsonIgnore
    @Column(name = "GROUP_ID")
    private int groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHANGE_AUTHOR",insertable = false,updatable = false)
    private User changeAuthor;

    @JsonIgnore
    @Column(name = "CHANGE_AUTHOR")
    private int changeAuthorId;

    public String getChangeDescription() {
        return changeDescription;
    }

    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }

    public Date getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(Date changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public User getChangeAuthor() {
        return changeAuthor;
    }

    public void setChangeAuthor(User changeAuthor) {
        this.changeAuthor = changeAuthor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getChangeAuthorId() {
        return changeAuthorId;
    }

    public void setChangeAuthorId(int changeAuthorId) {
        this.changeAuthorId = changeAuthorId;
    }
}
