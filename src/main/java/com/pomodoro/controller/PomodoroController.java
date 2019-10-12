package com.pomodoro.controller;

import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PomodoroController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public PomodoroController(SimpMessagingTemplate simpMessagingTemplate, UserService userService, UserRepository userRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @MessageMapping("/start/{username}")
    @SendTo("/pomodoro/start/{username}")
    public Pomodoro pomodoroStarted(@DestinationVariable String username) throws Exception {
        User user = userRepository.findUserByUsername(username);
        return userService.createPomodoroAndReturn(user);

    }

    @MessageMapping("/stop/{username}")
    @SendTo("/pomodoro/stop/{username}")
    public String pomodoroStopped(@DestinationVariable String username, @RequestBody Pomodoro pomodoro) throws Exception {
        User user = userRepository.findUserByUsername(username);
        userService.stopPomodoro(user,pomodoro);
        return "STOP";
    }


}
