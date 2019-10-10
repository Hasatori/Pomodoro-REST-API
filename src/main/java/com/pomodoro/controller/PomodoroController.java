package com.pomodoro.controller;

import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import com.pomodoro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PomodoroController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserService userService;

    @Autowired
    public PomodoroController(SimpMessagingTemplate simpMessagingTemplate, UserService userService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
    }

    @MessageMapping("/start/{username}")
    public void pomodoroStarted(@DestinationVariable String username, Pomodoro pomodoro) throws Exception {
        simpMessagingTemplate.convertAndSend("/pomodoro/start/" + username, pomodoro);
    }
    @MessageMapping("/stop/{username}")
    public void pomodoroStopped(@DestinationVariable String username, Pomodoro pomodoro) throws Exception {
        simpMessagingTemplate.convertAndSend("/pomodoro/stop/" + username, pomodoro);
    }
}
