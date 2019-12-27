package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity(name = "USER_GROUP_MESSAGE")
@Transactional
@IdClass(UserGroupId.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserGroupMessage  implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn
    private User user;



    @Id
    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private GroupMessage groupMessage;



    private Date readTimestamp;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GroupMessage getGroupMessage() {
        return groupMessage;
    }

    public void setGroupMessage(GroupMessage groupMessage) {
        this.groupMessage = groupMessage;
    }

    public Date getReadTimestamp() {
        return readTimestamp;
    }

    public void setReadTimestamp(Date readTimestamp) {
        this.readTimestamp = readTimestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(),groupMessage.getId(), readTimestamp);
    }

}
