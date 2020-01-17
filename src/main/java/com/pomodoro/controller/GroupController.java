package com.pomodoro.controller;

import com.pomodoro.model.*;
import com.pomodoro.utils.CheckUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GroupController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(GroupController.class);

    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    public ResponseEntity<?> getGroups(HttpServletRequest req) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Set<Group> userGroups = user.getGroups();
        return ResponseEntity.ok().body(userGroups);
    }

    @RequestMapping(value = "/groups/{groupName}", method = RequestMethod.POST)
    public Set<User> getUsersForGroup(HttpServletRequest req, @PathVariable(required = true) String groupName) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return userService.getUsersForGroup(groupName, user);
    }

    @RequestMapping(value = "/groups/{groupName}/fetch-chat-messages", method = RequestMethod.POST)
    public ResponseEntity<?> fetchChatMessages(HttpServletRequest req,
                                               @RequestBody GroupDataRequest groupDataRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupRepository.findPomodoroGroupByName(groupDataRequest.getGroupName()).get(0);
        Integer limit = groupDataRequest.getStop() - groupDataRequest.getStart();
        List<GroupMessage> groupMessages = groupMessageRepository.findLastMessagesByGroupIdWithinLimitAndOffset(group.getId(), limit, groupDataRequest.getStart());
        return ResponseEntity.ok(groupMessages);
    }

    @RequestMapping(value = "/groups/{groupName}/fetch-unread-messages", method = RequestMethod.POST)
    public ResponseEntity<?> fetchUnreadMessages(HttpServletRequest req,
                                                 @RequestBody GroupDataRequest groupDataRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupRepository.findPomodoroGroupByName(groupDataRequest.getGroupName()).get(0);
        List<GroupMessage> groupMessages = groupMessageRepository.findAllUnreadMessages(user.getId(), group.getId());
        return ResponseEntity.ok(groupMessages);
    }

    @RequestMapping(value = "/groups/{groupName}/mark-all-as-read", method = RequestMethod.POST)
    public ResponseEntity<?> markAllAsRead(HttpServletRequest req, @PathVariable String groupName) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupRepository.findPomodoroGroupByName(groupName).get(0);
        userGroupMessageRepository.markAllUserMessagesFromGroupAsRead(new Date(), user.getId(), group.getGroupMessages().stream().map(GroupMessage::getId).collect(Collectors.toList()));

        return ResponseEntity.ok(groupMessageRepository.findNewestGroupMessageByGroupId(group.getId()));
    }

    @RequestMapping(value = "/groups/{groupName}/fetch-changes", method = RequestMethod.POST)
    public ResponseEntity<?> fetchGroupChanges(HttpServletRequest req, @RequestBody GroupDataRequest groupDataRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupRepository.findPomodoroGroupByName(groupDataRequest.getGroupName()).get(0);
        Integer limit = groupDataRequest.getStop() - groupDataRequest.getStart();
        return ResponseEntity.ok(groupChangeRepository.findLastChangesByGroupIdWithinLimitAndOffset(group.getId(), limit, groupDataRequest.getStart()))
        ;
    }


    @RequestMapping(value = "/group/create", method = RequestMethod.POST)
    public void createGroup(HttpServletRequest req, @RequestBody Group group) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        userService.createGroup(user, group.getName(), group.isPublic());
    }

/*    @RequestMapping(value = "/group/addUser", method = RequestMethod.POST)
    public ResponseEntity<?> addUserToTheGroup(HttpServletRequest req, @Valid @RequestBody GroupRequest groupRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Map<String, String> responseEntity = new HashMap<>();
        int status = 400;
        Group originalGroup = groupRepository.findPomodoroGroupByNameAndOwnerId(groupRequest.getGroupName(), user.getId());
        User userToAdd = userRepository.findUserByUsername(groupRequest.getUsername());
        CheckUtils.basicGroupChecks(originalGroup, responseEntity, userToAdd);
        if (originalGroup != null && userToAdd != null && originalGroup.getUsers().stream().anyMatch(user1 -> user1.getUsername().equals(userToAdd.getUsername()))) {
            responseEntity.put("group", "User already is part of the group");
        }
        if (responseEntity.size() == 0) {
            status = 200;
            userService.addUserToGroup(originalGroup, userToAdd);
            responseEntity.put("success", String.format("User %s was successfully added to the group %s", groupRequest.getUsername(), originalGroup.getName()));
        }
        return ResponseEntity.status(status).body(responseEntity);
    }*/

    @RequestMapping(value = "/group/removeUser", method = RequestMethod.POST)
    public ResponseEntity<?> deleteUserFromTheGroup(HttpServletRequest req, @Valid @RequestBody GroupRequest groupRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Map<String, String> responseEntity = new HashMap<>();
        int status = 400;
        Group originalGroup = groupRepository.findPomodoroGroupByNameAndOwnerId(groupRequest.getGroupName(), user.getId());

        User userToRemove = userRepository.findUserByUsername(groupRequest.getUsername());
        CheckUtils.basicGroupChecks(originalGroup, responseEntity, userToRemove);
        if (responseEntity.size() == 0) {
            status = 200;
            userService.removeUserFromGroup(originalGroup, userToRemove);
            responseEntity.put("success", String.format("User %s was successfully removed from the group %s", groupRequest.getUsername(), originalGroup.getName()));
        }
        return ResponseEntity.status(status).body(responseEntity);
    }

}
