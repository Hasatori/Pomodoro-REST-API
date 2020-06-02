package com.pomodoro.websocket;


import com.pomodoro.config.SessionIdTokenPair;
import com.pomodoro.model.change.GroupChange;
import com.pomodoro.model.dto.IsUserTyping;
import com.pomodoro.model.dto.MessageAnswer;
import com.pomodoro.model.group.Group;
import com.pomodoro.model.message.DirectMessage;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.reaction.UserReaction;
import com.pomodoro.model.request.GroupUserRequest;
import com.pomodoro.model.todo.GroupToDo;
import com.pomodoro.model.user.User;
import com.pomodoro.utils.CheckUtils;
import com.pomodoro.utils.DateUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.pomodoro.config.WebSocketConfig.SESSIONID_USER;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GroupWebsocket extends AbstractSocket {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static final String SEND_MESSAGE_PATH = "/group/%s/chat";
    private static final String RESEND_MESSAGE_PATH = "/group/%s/chat/resend";
    private static final String TYPING_PATH = "/group/%s/chat/typing";

    @MessageMapping("/group/{groupName}/chat")
    public void readAndWriteMessage(Principal principal, @DestinationVariable String groupName, @RequestBody String message) throws Exception {
        User author = (User) principal;
        Group group = groupService.getGroup(author, groupName);
        GroupMessage groupMessage = groupService.createGroupMessage(author, group, message);
        sendMessageToGroup(groupMessage, group, SEND_MESSAGE_PATH);
    }

    @MessageMapping("/group/{groupName}/chat/react")
    public void resendMessage(Principal principal, @DestinationVariable String groupName, @RequestBody UserReaction userReaction) throws Exception {
        User author = (User) principal;
        Group group = groupService.getGroup(author, groupName);
        UserReaction foundReaction = userReactionRepository.findUserReactionByAuthorIdAndMessageId(author.getId(), userReaction.getMessageId());
        createOrUpdateReaction(foundReaction,userReaction,author);
        GroupMessage groupMessage = groupMessageRepository.getOne(userReaction.getMessageId());
        sendMessageToGroup(groupMessage, group, RESEND_MESSAGE_PATH);
    }

    @MessageMapping("/group/{groupName}/answer")
    public void answerMessage(Principal principal, @DestinationVariable String groupName, @RequestBody MessageAnswer messageAnswer) throws Exception {
        User author = (User) principal;
        Group group = groupService.getGroup(author, groupName);
        GroupMessage answer = groupService.createAnswerForMessage(author, group, messageAnswer.getAnswerValue(), groupMessageRepository.getOne(messageAnswer.getAnsweredMessage().getId()));
        sendMessageToGroup(answer, group, SEND_MESSAGE_PATH);
    }

    @MessageMapping("/group/{groupName}/chat/typing")
    public void isUserTyping(Principal principal, @DestinationVariable String groupName, @RequestBody Boolean typing) throws Exception {
        User author = (User) principal;
        Group group = groupService.getGroup(author, groupName);
        IsUserTyping isUserTyping = new IsUserTyping();
        isUserTyping.setUser(author);
        isUserTyping.setIsTyping(typing);
        group.getUsers()
                .stream()
                .filter(user -> !user.getUsername().equals(author.getUsername()))
                .forEach(user -> {
                    List<SessionIdTokenPair> userSessionIdTokenPairs = SESSIONID_USER.get(user.getUsername());
                    if (userSessionIdTokenPairs != null) {
                        userSessionIdTokenPairs.forEach(sessionIdTokenPair -> {
                            simpMessagingTemplate.convertAndSend(String.format(TYPING_PATH, sessionIdTokenPair.getToken()), isUserTyping);
                        });
                    }

                });
    }

    @MessageMapping("/group/{name}/change")
    @SendTo("/group/{name}/change")
    public GroupChange readAndWriteChange(Principal principal, @DestinationVariable String groupName, @RequestBody GroupChange groupChange) throws Exception {
        User author = (User) principal;
        Group group = groupRepository.findPomodoroGroupByName(groupName).get(0);
        groupChange.setChangeAuthor(author);
        groupChange.setChangeAuthorId(author.getId());
        groupChange.setGroup(group);
        groupChange.setGroupId(group.getId());
        groupChange.setChangeTimestamp(DateUtils.getCurrentLocalDateTimeUtc());
        return groupChangeRepository.save(groupChange);
    }

    @MessageMapping("/group/{name}/chat/emoji")
    @SendTo("/group/{name}/chat/emoji")
    public GroupMessage reactToGroupMessage(Principal principal, @DestinationVariable String groupName, @RequestBody UserReaction userReaction) {
        User user = (User) principal;
        userReactionRepository.setReaction(userReaction.getEmoji(), user.getId(), userReaction.getMessageId());
        GroupMessage groupMessage = groupMessageRepository.findGroupMessageById(userReaction.getMessageId());
        Optional<UserReaction> optionalUserGroupMessage = groupMessage.getReactions().stream().filter(userGroupMessage -> userGroupMessage.getAuthor().getUsername().equals(principal.getName())).findFirst();
        userReaction = optionalUserGroupMessage.orElse(null);
        if (userReaction == null) {
            throw new IllegalStateException("User group messageId doest not exist");
        }
        return groupMessage;
    }

    @MessageMapping("/group/{name}/group-members")
    @SendTo("/group/{name}/group-members")
    public Object groupMembers(Principal principal, @DestinationVariable String groupName, @Valid @RequestBody GroupUserRequest groupUserRequest) throws Exception {
        User user = (User) principal;
        Map<String, String> responseEntity = new HashMap<>();
        Group originalGroup = groupRepository.findPomodoroGroupByNameAndOwnerId(groupUserRequest.getGroupName(), user.getId());
        User userToAdd = userRepository.findUserByUsername(groupUserRequest.getUsername());
        CheckUtils.basicGroupChecks(originalGroup, responseEntity, userToAdd);
        if (originalGroup != null && userToAdd != null && originalGroup.getUsers().stream().anyMatch(user1 -> user1.getUsername().equals(userToAdd.getUsername()))) {
            responseEntity.put("group", "User already is part of the group");
        }
        if (responseEntity.size() == 0) {
            userService.addUserToGroup(originalGroup, userToAdd);
            return userToAdd;
        }
        return responseEntity;
    }

    @MessageMapping("/group/{name}/todos")
    @SendTo("/group/{name}/todos")
    public Object readAndWriteTodos(Principal principal, @DestinationVariable String groupName, @RequestBody GroupToDo groupToDo) throws Exception {
        User user = (User) principal;
        groupToDo = groupTodoRepository.save(groupToDo);
        groupToDo = groupTodoRepository.findGroupToDoById(groupToDo.getId());
        return groupToDo;
    }

    private void sendMessageToGroup(GroupMessage groupMessage, Group group, String path) {
        group.getUsers().forEach(user -> {
            List<SessionIdTokenPair> userSessionIdTokenPairs = SESSIONID_USER.get(user.getUsername());
            if (userSessionIdTokenPairs != null) {
                userSessionIdTokenPairs.forEach(sessionIdTokenPair -> {
                    simpMessagingTemplate.convertAndSend(String.format(path, sessionIdTokenPair.getToken()), groupMessage);
                });
            }

        });
    }
}
