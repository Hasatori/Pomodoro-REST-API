package com.pomodoro.websocket;

import com.pomodoro.model.message.DirectMessage;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.reaction.UserReaction;
import com.pomodoro.model.request.DirectMessageReaction;
import com.pomodoro.model.todo.UserToDo;
import com.pomodoro.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.pomodoro.config.WebSocketConfig.USER_TOKENS;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserWebsocket extends AbstractSocket {

    private static final String SEND_MESSAGE_PATH = "/user/%s/chat";
    private static final String RESEND_MESSAGE_PATH = "/user/%s/chat/resend";

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/user/{username}/todos")
    @SendTo("/user/{username}/todos")
    public UserToDo readAndWriteTodos(Principal principal, @DestinationVariable String username, @RequestBody UserToDo userToDo) throws Exception {
        User user = (User) principal;
        userToDo = userTodoRepository.save(userToDo);
        userToDo = userTodoRepository.findUserToDoById(userToDo.getId());
        return userToDo;
    }

    @MessageMapping("/user/{username}/chat")
    public void readAndWriteMessage(Principal principal, @DestinationVariable String username, @RequestBody String message) throws Exception {
        User author = (User) principal;
        User recipient = userService.loadUserByUsername(username);
        String authorToken = USER_TOKENS.get(author.getUsername());
        String recipientToken = USER_TOKENS.get(recipient.getUsername());
        DirectMessage directMessage = userService.createDirectMessage(author, recipient, message);
        if (authorToken != null) {
            simpMessagingTemplate.convertAndSend(String.format(SEND_MESSAGE_PATH, authorToken), directMessage);
        }
        if (recipientToken != null) {
            simpMessagingTemplate.convertAndSend(String.format(SEND_MESSAGE_PATH, recipientToken), directMessage);
        }
    }

    @MessageMapping("/user/{username}/chat/react")
    public void resendMessage(Principal principal, @DestinationVariable String username, @RequestBody UserReaction userReaction) throws Exception {
        User author = (User) principal;
        User recipient = userService.loadUserByUsername(username);
        String authorToken = USER_TOKENS.get(author.getUsername());
        String recipientToken = USER_TOKENS.get(recipient.getUsername());
        UserReaction foundReaction = userReactionRepository.findUserReactionByAuthorIdAndMessageId(author.getId(), userReaction.getMessageId());
        if (foundReaction!= null){
            foundReaction.setEmoji(userReaction.getEmoji());
            userReactionRepository.save(foundReaction);
        }else{
            userReaction.setAuthorId(author.getId());
            userReactionRepository.save(userReaction);
        }
        DirectMessage directMessage = directMessageRepository.getOne(userReaction.getMessageId());
       List<UserReaction> reactionList= directMessage.getReactions();
        if (authorToken != null) {
            simpMessagingTemplate.convertAndSend(String.format(RESEND_MESSAGE_PATH, authorToken), directMessage);
        }
        if (recipientToken != null) {
            simpMessagingTemplate.convertAndSend(String.format(RESEND_MESSAGE_PATH, recipientToken), directMessage);
        }
    }
}
