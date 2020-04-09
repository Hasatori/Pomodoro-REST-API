package com.pomodoro.websocket;

import com.pomodoro.model.user.Pomodoro;
import com.pomodoro.model.user.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PomodoroWebsocket extends AbstractSocket {


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
        userService.stopPomodoro(user, pomodoro);
        return "STOP";
    }


}
