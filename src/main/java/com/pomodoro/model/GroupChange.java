package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "GROUP_CHANGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
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
    private Integer groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHANGE_AUTHOR",insertable = false,updatable = false)
    private User changeAuthor;

    @JsonIgnore
    @Column(name = "CHANGE_AUTHOR")
    private Integer changeAuthorId;

}
