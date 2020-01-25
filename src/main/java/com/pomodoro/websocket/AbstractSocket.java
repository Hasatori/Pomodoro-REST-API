package com.pomodoro.websocket;

import com.pomodoro.model.GroupChange;
import com.pomodoro.repository.*;
import com.pomodoro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class AbstractSocket {

    @Autowired
    protected UserService userService;

    @Autowired
    protected SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected GroupMessageRepository groupMessageRepository;
    @Autowired
    protected GroupRepository groupRepository;
    @Autowired
    protected UserGroupMessageRepository userGroupMessageRepository;

    @Autowired
    protected GroupChangeRepository groupChangeRepository;

    @Autowired
    protected GroupTodoRepository groupTodoRepository;
}
