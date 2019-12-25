package com.pomodoro.websocket;

import com.pomodoro.model.ChatMessage;
import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Date;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GroupWebsocket extends AbstractSocket {

    GroupWebsocket(UserService userService, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository) {
        super(userService, simpMessagingTemplate, userRepository);
    }

    @MessageMapping("/group/{groupName}/chat")
    @SendTo("/group/{groupName}/chat")
    public ChatMessage readAndWriteMessage(Principal principal, @RequestBody String message) throws Exception {
        User user = (User) principal;
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setAuthor(principal.getName());
        chatMessage.setValue(message);
        chatMessage.setTimestamp(new Date());
        return chatMessage;
    }
}
