package com.pomodoro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;


public class UserGroupId  implements Serializable {


 User user;
 GroupMessage groupMessage;

}
