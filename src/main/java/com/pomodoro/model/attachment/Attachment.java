package com.pomodoro.model.attachment;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "ATTACHMENT")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Attachment {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String name;

    private String format;
}
