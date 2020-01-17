package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "GROUP_MESSAGE_CHANGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class GroupMessageChange {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "GROUP_MESSAGE_ID", insertable = false, updatable = false)
    private GroupMessage groupMessage;

    @JsonIgnore
    @Column(name = "GROUP_MESSAGE_ID")
    private int groupMessageId;

    @CreationTimestamp
    private Date creationTimestamp;

    private String oldValue;
    private String newValue;

}
