package com.pomodoro.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "GROUP_MESSAGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class GroupMessage {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String value;

    @Nullable
    private String attachment;

    private Date timestamp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID",insertable = false,updatable = false)
    private User author;

    @JsonIgnore
    @Column(name = "AUTHOR_ID")
    private Integer authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID",insertable = false,updatable = false)
    private Group group ;

    @JsonIgnore
    @Column(name = "GROUP_ID")
    private Integer groupId;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "groupMessage")
    private List<UserGroupMessage> relatedGroupMessages;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "groupMessage")
    private List<GroupMessageChange> changes;


}
