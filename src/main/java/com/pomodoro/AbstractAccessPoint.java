package com.pomodoro;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.service.IGroupService;
import com.pomodoro.service.IStorageService;
import com.pomodoro.service.IUserService;
import com.pomodoro.service.repository.*;
import com.pomodoro.service.serviceimplementation.UserService;
import com.pomodoro.utils.RequestDataNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class AbstractAccessPoint {

    private static final Logger log = LoggerFactory.getLogger(AbstractAccessPoint.class);
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



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodParameterValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestDataNotValidException.class)
    public Map<String, String> handleRequestValidationExceptions(
            RequestDataNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getErrorList().forEach(error -> {
            errors.put(error.getFieldName(), error.getMessage());
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleRuntimeException(
            RuntimeException ex) {
        log.error("Runtime exception in controller {}", ex);
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", "Unknown error");
        return errors;
    }
}
