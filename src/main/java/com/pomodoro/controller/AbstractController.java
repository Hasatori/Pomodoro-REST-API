package com.pomodoro.controller;

import com.pomodoro.AbstractAccessPoint;
import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.service.IGroupService;
import com.pomodoro.service.repository.*;
import com.pomodoro.service.IStorageService;
import com.pomodoro.service.IUserService;
import com.pomodoro.service.serviceimplementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;

public class AbstractController  extends AbstractAccessPoint {

    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected JwtTokenUtil jwtTokenUtil;
    @Autowired
    protected UserService userService;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected GroupRepository groupRepository;
    @Autowired
    protected GroupInvitationRepository groupInvitationRepository;
    @Autowired
    protected GroupMessageRepository groupMessageRepository;
    @Autowired
    protected UserGroupMessageRepository userGroupMessageRepository;
    @Autowired
    protected GroupChangeRepository groupChangeRepository;
    @Autowired
    protected GroupTodoRepository groupTodoRepository;

    @Autowired
    protected UserTodoRepository userTodoRepository;

    @Qualifier("basicStorageService")
    @Autowired
    protected IStorageService storageService;

    @Qualifier("limitedUserService")
    @Autowired
    protected IUserService userService2;

    @Qualifier("limitedGroupService")
    @Autowired
    protected IGroupService groupService;
    

}
