package com.pomodoro.websocket;

import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import com.pomodoro.repository.GroupMessageRepository;
import com.pomodoro.repository.GroupRepository;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PomodoroWebsocket extends AbstractSocket{

    PomodoroWebsocket(UserService userService, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository, GroupMessageRepository groupMessageRepository, GroupRepository groupRepository) {
        super(userService, simpMessagingTemplate, userRepository, groupMessageRepository, groupRepository);
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
