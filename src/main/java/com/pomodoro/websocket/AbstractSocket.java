package com.pomodoro.websocket;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;

public class AbstractSocket {

    final UserService userService;

    final SimpMessagingTemplate simpMessagingTemplate;

    final UserRepository userRepository;

   AbstractSocket(UserService userService, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository) {
        this.userService = userService;
       this.simpMessagingTemplate = simpMessagingTemplate;
       this.userRepository = userRepository;
   }
}
