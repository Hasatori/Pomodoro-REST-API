package com.pomodoro.model.reaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.message.Message;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "REACTION")
@Getter
@Setter
@IdClass(UserMessageId.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class UserReaction implements Serializable {

    @JsonIgnore
    @Id
    @Column(name = "AUTHOR")
   private Integer authorId;

    @JsonIgnore
    @Id
    @Column(name = "MESSAGE")
    private Integer messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR",insertable = false,updatable = false)
    private User author;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE", nullable = false, insertable = false, updatable = false)
    private Message message;

    private LocalDateTime readTimestamp;

    @Nullable
    private String emoji;

    @Nullable
    public String getEmoji() {
        return emoji;
    }

}
