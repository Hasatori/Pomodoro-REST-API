package com.pomodoro.model.reaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "USER_REACTION")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserReaction implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private Date readTimestamp;

    @Nullable
    private String reaction;

    @Nullable
    public String getReaction() {
        return reaction;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR",insertable = false,updatable = false)
    private User author;

    @JsonIgnore
    @Column(name = "AUTHOR")
    private Integer authorId;

}
