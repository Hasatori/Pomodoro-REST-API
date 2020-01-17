package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
public class UserGroupMessage implements Serializable {

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

    @Nullable
    private String reaction;

    @Nullable
    public String getReaction() {
        return reaction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), groupMessage.getId(), readTimestamp);
    }

}
