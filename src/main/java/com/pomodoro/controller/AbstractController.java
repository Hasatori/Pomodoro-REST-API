package com.pomodoro.controller;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.repository.*;
import com.pomodoro.service.StorageService;
import com.pomodoro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class AbstractController {

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

    @Autowired
    protected StorageService storageService;

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
