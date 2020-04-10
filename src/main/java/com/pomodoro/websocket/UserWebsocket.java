package com.pomodoro.websocket;

import com.pomodoro.model.message.DirectMessage;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.todo.UserToDo;
import com.pomodoro.model.user.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserWebsocket extends AbstractSocket {

    @MessageMapping("/user/{username}/todos")
    @SendTo("/user/{username}/todos")
    public UserToDo readAndWriteTodos(Principal principal, @DestinationVariable String username, @RequestBody UserToDo userToDo) throws Exception {
        User user = (User) principal;
        userToDo = userTodoRepository.save(userToDo);
        userToDo = userTodoRepository.findUserToDoById(userToDo.getId());
        return userToDo;
    }

    @MessageMapping("/user/{username}/chat")
    @SendTo("/user/{username}/chat")
    public DirectMessage readAndWriteMessage(Principal principal, @DestinationVariable String username, @RequestBody String message) throws Exception {
        User author = (User) principal;
        User recipient=userService.loadUserByUsername(username);
        return userService.createDirectMessage(author,recipient,message);
    }

    @MessageMapping("/user/{username}/resend")
    @SendTo("/user/{username}/resend")
    public DirectMessage resendMessage(DirectMessage directMessage) throws Exception {
        return directMessage;
    }
}
