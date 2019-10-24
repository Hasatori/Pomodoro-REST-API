package com.pomodoro.controller;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;

public class AbstractController {

    final AuthenticationManager authenticationManager;

    final JwtTokenUtil jwtTokenUtil;

    final UserService userService;


    AbstractController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

}
