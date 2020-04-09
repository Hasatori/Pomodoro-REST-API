package com.pomodoro.utils;

import com.pomodoro.model.group.Group;
import com.pomodoro.model.user.User;

import java.util.Map;

public class CheckUtils {

    private CheckUtils(){}



    public static void basicGroupChecks(Group originalGroup, Map<String, String> responseEntity, User user) {
        if (originalGroup == null) {
            responseEntity.put("group", "Either you are not the owner of the group or group doest not exist");
        }
        if (user == null) {
            responseEntity.put("username", "Given user does not exist");
        }

    }
}
