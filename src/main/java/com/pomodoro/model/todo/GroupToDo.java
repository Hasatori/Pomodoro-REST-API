package com.pomodoro.model.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pomodoro.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "GROUP_TO_DO")
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class GroupToDo extends ToDo {
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "USER_GROUP_TO_DO",
            joinColumns = @JoinColumn(name = "TO_DO_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    Set<User> assignedUsers;

}
