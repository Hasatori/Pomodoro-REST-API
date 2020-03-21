package com.pomodoro.model;

import com.pomodoro.model.message.GroupMessage;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserGroupId  implements Serializable {


 User user;
 GroupMessage groupMessage;

}
