package com.pomodoro.model.attachment;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "ATTACHMENT")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Attachment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    private String format;
}
