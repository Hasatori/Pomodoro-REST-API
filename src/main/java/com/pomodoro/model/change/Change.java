package com.pomodoro.model.change;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pomodoro.model.ChangeType;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@Entity(name = "CHANGE")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Change {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "change_generator")
    @SequenceGenerator(name="change_generator", sequenceName = "change_seq")
    protected Integer id;

    @Enumerated(EnumType.STRING)
    protected ChangeType changeType;

    protected LocalDateTime changeTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHANGE_AUTHOR", insertable = false, updatable = false)
    protected User changeAuthor;

    @JsonIgnore
    @Column(name = "CHANGE_AUTHOR")
    protected Integer changeAuthorId;

}
