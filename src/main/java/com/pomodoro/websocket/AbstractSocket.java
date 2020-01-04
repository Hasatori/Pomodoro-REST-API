package com.pomodoro.websocket;

import com.pomodoro.repository.GroupMessageRepository;
import com.pomodoro.repository.GroupRepository;
import com.pomodoro.repository.UserGroupMessageRepository;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class AbstractSocket {

    final UserService userService;

    final SimpMessagingTemplate simpMessagingTemplate;

    final UserRepository userRepository;
    final GroupMessageRepository groupMessageRepository;
    final GroupRepository groupRepository;
final UserGroupMessageRepository userGroupMessageRepository;


    AbstractSocket(UserService userService, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository, GroupMessageRepository groupMessageRepository, GroupRepository groupRepository, UserGroupMessageRepository userGroupMessageRepository) {
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
        this.groupMessageRepository = groupMessageRepository;
        this.groupRepository = groupRepository;
        this.userGroupMessageRepository = userGroupMessageRepository;
    }
}
