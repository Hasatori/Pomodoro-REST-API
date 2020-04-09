package com.pomodoro.model.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "GROUP_INVITATION")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class GroupInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
    private Group group;

    @JsonIgnore
    @Column(name = "GROUP_ID")
    private Integer groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVITED_USER_ID",insertable = false,updatable = false)
    private User invitedUser;

    @JsonIgnore
    @Column(name = "INVITED_USER_ID")
    private Integer invitedUserId;

    private Boolean accepted;

    private Boolean refused;

}
