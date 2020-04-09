package com.pomodoro.model.reaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "REACTION")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserReaction implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    protected Integer id;

    protected Date readTimestamp;

    @Nullable
    protected String reaction;

    @Nullable
    protected String getReaction() {
        return reaction;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR",insertable = false,updatable = false)
    protected User author;

    @JsonIgnore
    @Column(name = "AUTHOR")
    protected Integer authorId;

}
