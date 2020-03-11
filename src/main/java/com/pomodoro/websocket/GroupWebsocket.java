package com.pomodoro.websocket;

import com.pomodoro.model.*;
import com.pomodoro.model.request.GroupUserRequest;
import com.pomodoro.utils.CheckUtils;
import com.pomodoro.utils.DateUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GroupWebsocket extends AbstractSocket {

    @MessageMapping("/group/{groupName}/chat")
    @SendTo("/group/{groupName}/chat")
    public GroupMessage readAndWriteMessage(Principal principal, @DestinationVariable String groupName, @RequestBody String message) throws Exception {
        User author = (User) principal;
        Group group = groupService.getGroup(author, groupName);
        return groupService.createGroupMessage(author,group,message);
    }

    @MessageMapping("/group/{groupName}/chat/resend")
    @SendTo("/group/{groupName}/chat/resend")
    public GroupMessage resendMessage(GroupMessage groupMessage) throws Exception {
        return groupMessage;
    }

    @MessageMapping("/group/{groupName}/change")
    @SendTo("/group/{groupName}/change")
    public GroupChange readAndWriteChange(Principal principal, @DestinationVariable String groupName, @RequestBody GroupChange groupChange) throws Exception {
        User author = (User) principal;
        Group group = groupRepository.findPomodoroGroupByName(groupName).get(0);
        groupChange.setChangeAuthor(author);
        groupChange.setChangeAuthorId(author.getId());
        groupChange.setGroup(group);
        groupChange.setGroupId(group.getId());
        groupChange.setChangeTimestamp(DateUtils.getCurrentDateUtc());
        return groupChangeRepository.save(groupChange);
    }

    @MessageMapping("/group/{groupName}/chat/reaction")
    @SendTo("/group/{groupName}/chat/reaction")
    public GroupMessage reactToGroupMessage(Principal principal, @DestinationVariable String groupName, @RequestBody GroupMessageReaction groupMessageReaction) {
        User user = (User) principal;
        userGroupMessageRepository.setReaction(groupMessageReaction.getReaction(), user.getId(), groupMessageReaction.getGroupMessageId());
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

    @MessageMapping("/group/{groupName}/todos")
    @SendTo("/group/{groupName}/todos")
    public Object readAndWriteTodos(Principal principal, @DestinationVariable String groupName, @RequestBody GroupToDo groupToDo) throws Exception {
        User user = (User) principal;
        groupToDo = groupTodoRepository.save(groupToDo);
        groupToDo = groupTodoRepository.findGroupToDoById(groupToDo.getId());
        return groupToDo;
    }
}
