package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "GROUP_MESSAGE_CHANGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GroupMessageChange {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "GROUP_MESSAGE_ID", insertable = false, updatable = false)
    private GroupMessage groupMessage;

    @JsonIgnore
    @Column(name = "GROUP_MESSAGE_ID")
    private int groupMessageId;

    @CreationTimestamp
    private Date creationTimestamp;

    private String oldValue;
    private String newValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GroupMessage getGroupMessage() {
        return groupMessage;
    }

    public void setGroupMessage(GroupMessage groupMessage) {
        this.groupMessage = groupMessage;
    }

    public int getGroupMessageId() {
        return groupMessageId;
    }

    public void setGroupMessageId(int groupMessageId) {
        this.groupMessageId = groupMessageId;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
