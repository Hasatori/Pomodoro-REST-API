package com.pomodoro.websocket;

import com.pomodoro.config.SessionIdTokenPair;
import com.pomodoro.model.dto.IsUserTyping;
import com.pomodoro.model.dto.MessageAnswer;
import com.pomodoro.model.message.DirectMessage;
import com.pomodoro.model.reaction.UserReaction;
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

import java.security.Principal;
import java.util.List;

import static com.pomodoro.config.WebSocketConfig.SESSIONID_USER;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserWebsocket extends AbstractSocket {

    private static final String SEND_MESSAGE_PATH = "/user/%s/chat";
    private static final String RESEND_MESSAGE_PATH = "/user/%s/chat/resend";
    private static final String TYPING_PATH = "/user/%s/chat/typing";
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
        DirectMessage directMessage = userService.createDirectMessage(author, recipient, message);
        sendDirectMessageToAll(author, recipient, directMessage, SEND_MESSAGE_PATH);
    }

    @MessageMapping("/user/{username}/chat/react")
    public void resendMessage(Principal principal, @DestinationVariable String username, @RequestBody UserReaction userReaction) throws Exception {
        User author = (User) principal;
        User recipient = userService.loadUserByUsername(username);
        UserReaction foundReaction = userReactionRepository.findUserReactionByAuthorIdAndMessageId(author.getId(), userReaction.getMessageId());
        createOrUpdateReaction(foundReaction, userReaction, author);
        DirectMessage directMessage = directMessageRepository.getOne(userReaction.getMessageId());
        sendDirectMessageToAll(author, recipient, directMessage, RESEND_MESSAGE_PATH);
    }

    @MessageMapping("/user/{username}/answer")
    public void answerMessage(Principal principal, @DestinationVariable String username, @RequestBody MessageAnswer messageAnswer) throws Exception {
        User author = (User) principal;
        User recipient = userService.loadUserByUsername(username);
        DirectMessage answer = userService.createAnswerForMessage(author, recipient, messageAnswer.getAnswerValue(), directMessageRepository.getOne(messageAnswer.getAnsweredMessage().getId()));
        sendDirectMessageToAll(author, recipient, answer, SEND_MESSAGE_PATH);
    }

    private void sendDirectMessageToAll(User author, User recipient, DirectMessage directMessage, String path) {
        List<SessionIdTokenPair> authorSessionIdTokensPairs = SESSIONID_USER.get(author.getUsername());
        List<SessionIdTokenPair> recipientSessionIdTokenPairs = SESSIONID_USER.get(recipient.getUsername());
        authorSessionIdTokensPairs.forEach(authorToken -> {
            simpMessagingTemplate.convertAndSend(String.format(path, authorToken.getToken()), directMessage);
        });
        recipientSessionIdTokenPairs.forEach(recipientToken -> {
            simpMessagingTemplate.convertAndSend(String.format(path, recipientToken.getToken()), directMessage);
        });
    }

    @MessageMapping("/user/{username}/chat/typing")
    public void isUserTyping(Principal principal, @DestinationVariable String username, @RequestBody Boolean typing) throws Exception {
        User author = (User) principal;
        User recipient = userService.loadUserByUsername(username);
        List<SessionIdTokenPair> recipientSessionIdTokenPairs = SESSIONID_USER.get(recipient.getUsername());
        IsUserTyping isUserTyping = new IsUserTyping();
        isUserTyping.setUser(author);
        isUserTyping.setIsTyping(typing);
        recipientSessionIdTokenPairs.forEach(recipientToken -> {
            simpMessagingTemplate.convertAndSend(String.format(TYPING_PATH, recipientToken.getToken()), isUserTyping);
        });
    }

}
