package com.pomodoro.model.reaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.message.DirectMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;


@Entity(name = "DIRECT_MESSAGE_REACTION")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"DIRECT_MESSAGE", "AUTHOR"})
})
@Transactional
@IdClass(UserGroupId.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class DirectMessageReaction extends UserReaction implements Serializable {

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIRECT_MESSAGE", nullable = false, insertable = false, updatable = false)
    private DirectMessage directMessage;

    @JsonIgnore
    @Column(name = "DIRECT_MESSAGE")
    private Integer directMessageId;

}
