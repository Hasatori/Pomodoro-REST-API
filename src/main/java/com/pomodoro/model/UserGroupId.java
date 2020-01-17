package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
public class UserGroupId  implements Serializable {


 User user;
 GroupMessage groupMessage;

}
