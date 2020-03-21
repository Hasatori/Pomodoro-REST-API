package com.pomodoro.model.change;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.group.Group;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity(name = "GROUP_CHANGE")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class GroupChange extends Change {

    private String oldName;
    private String newName;
    private String oldDescription;
    private String newDescription;
    private String oldLayoutImage;
    private String newLayoutImage;
    private Boolean oldIsPublic;
    private Boolean newIsPublic;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP", nullable = false, insertable = false, updatable = false)
    private Group group;
    @JsonIgnore
    private Integer groupId;

}
