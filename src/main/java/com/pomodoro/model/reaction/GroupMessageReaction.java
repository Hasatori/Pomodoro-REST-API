package com.pomodoro.model.reaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.message.GroupMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;


@Entity(name = "GROUP_MESSAGE_REACTION")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"GROUP_MESSAGE_ID", "AUTHOR"})
})
@Transactional
@IdClass(UserGroupId.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class GroupMessageReaction extends UserReaction implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_MESSAGE",insertable = false,updatable = false)
    private GroupMessage groupMessage;

    @JsonIgnore
    @Column(name = "GROUP_MESSAGE_ID")
    private Integer groupMessageId;

}
