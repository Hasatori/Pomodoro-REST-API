package com.pomodoro.websocket;

import com.pomodoro.AbstractAccessPoint;
import com.pomodoro.service.repository.*;
import com.pomodoro.service.IStorageService;
import com.pomodoro.service.serviceimplementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class AbstractSocket   extends AbstractAccessPoint {

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
    protected UserReactionRepository userReactionRepository;

    @Autowired
    protected GroupChangeRepository groupChangeRepository;

    @Autowired
    protected GroupTodoRepository groupTodoRepository;

    @Autowired
    protected  UserTodoRepository userTodoRepository;

    @Qualifier("basicStorageService")
    @Autowired
    protected IStorageService IStorageService;
}
