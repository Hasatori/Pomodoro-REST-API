package com.pomodoro.controller;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.repository.*;
import com.pomodoro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class AbstractController {

    final AuthenticationManager authenticationManager;

    final JwtTokenUtil jwtTokenUtil;

    final UserService userService;

    final UserRepository userRepository;

    final GroupRepository groupRepository;
    final GroupInvitationRepository groupInvitationRepository;
    final GroupMessageRepository groupMessageRepository;
    final UserGroupMessageRepository userGroupMessageRepository;

    AbstractController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, UserRepository userRepository, GroupRepository groupRepository, GroupInvitationRepository groupInvitationRepository, GroupMessageRepository groupMessageRepository, UserGroupMessageRepository userGroupMessageRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupInvitationRepository = groupInvitationRepository;
        this.groupMessageRepository = groupMessageRepository;
        this.userGroupMessageRepository = userGroupMessageRepository;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
