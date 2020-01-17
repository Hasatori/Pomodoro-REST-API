package com.pomodoro.websocket;

import com.pomodoro.repository.GroupMessageRepository;
import com.pomodoro.repository.GroupRepository;
import com.pomodoro.repository.UserGroupMessageRepository;
import com.pomodoro.repository.UserRepository;
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


}
