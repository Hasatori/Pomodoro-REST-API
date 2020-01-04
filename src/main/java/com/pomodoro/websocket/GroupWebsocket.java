package com.pomodoro.websocket;

import com.pomodoro.model.*;
import com.pomodoro.repository.GroupMessageRepository;
import com.pomodoro.repository.GroupRepository;
import com.pomodoro.repository.UserGroupMessageRepository;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import com.pomodoro.utils.CheckUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GroupWebsocket extends AbstractSocket {

    GroupWebsocket(UserService userService, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository, GroupMessageRepository groupMessageRepository, GroupRepository groupRepository, UserGroupMessageRepository userGroupMessageRepository) {
        super(userService, simpMessagingTemplate, userRepository, groupMessageRepository, groupRepository, userGroupMessageRepository);
    }

    @MessageMapping("/group/{groupName}/chat")
    @SendTo("/group/{groupName}/chat")
    public GroupMessage readAndWriteMessage(Principal principal, @DestinationVariable String groupName, @RequestBody String message) throws Exception {
        User author = (User) principal;
        Group group = groupRepository.findPomodoroGroupByName(groupName).get(0);
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setAuthor(author);
        groupMessage.setAuthorId(author.getId());
        groupMessage.setValue(message);
        groupMessage.setTimestamp(new Date());
        groupMessage.setGroup(group);
        groupMessage.setGroupId(group.getId());
        groupMessage.setRelatedGroupMessages(new ArrayList<>());
        groupMessage = groupMessageRepository.save(groupMessage);
        for (User user : groupMessage.getGroup().getUsers()) {
            UserGroupMessage userGroupMessage = new UserGroupMessage();
            userGroupMessage.setUser(user);
            if (user.getUsername().equals(author.getUsername())) {
                userGroupMessage.setReadTimestamp(new Date());
            }

            userGroupMessage.setGroupMessage(groupMessage);

            groupMessage.getRelatedGroupMessages().add(userGroupMessage);
        }
        groupMessage = groupMessageRepository.save(groupMessage);
        return groupMessage;
    }

    @MessageMapping("/group/{groupName}/chat/reaction")
    @SendTo("/group/{groupName}/chat/reaction")
    public GroupMessage reactToGroupMessage(Principal principal, @DestinationVariable String groupName, @RequestBody GroupMessageReaction groupMessageReaction) {
        User user = (User) principal;
        userGroupMessageRepository.setReaction(groupMessageReaction.getReaction(),user.getId(),groupMessageReaction.getGroupMessageId());
        GroupMessage groupMessage = groupMessageRepository.findGroupMessageById(groupMessageReaction.getGroupMessageId());
        Optional<UserGroupMessage> optionalUserGroupMessage = groupMessage.getRelatedGroupMessages().stream().filter(userGroupMessage -> userGroupMessage.getUser().getUsername().equals(principal.getName())).findFirst();
        UserGroupMessage userGroupMessage = optionalUserGroupMessage.orElse(null);
        if (userGroupMessage == null) {
            throw new IllegalStateException("User group message doest not exist");
        }
        return groupMessage;
    }

    @MessageMapping("/group/{groupName}/group-members")
    @SendTo("/group/{groupName}/group-members")
    public Object groupMembers(Principal principal, @DestinationVariable String groupName, @Valid @RequestBody GroupRequest groupRequest) throws Exception {
        User user = (User) principal;
        Map<String, String> responseEntity = new HashMap<>();
        Group originalGroup = groupRepository.findPomodoroGroupByNameAndOwnerId(groupRequest.getGroupName(), user.getId());
        User userToAdd = userRepository.findUserByUsername(groupRequest.getUsername());
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
}