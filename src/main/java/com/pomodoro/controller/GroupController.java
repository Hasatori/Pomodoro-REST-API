package com.pomodoro.controller;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.model.*;
import com.pomodoro.repository.GroupMessageRepository;
import com.pomodoro.repository.GroupRepository;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GroupController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(GroupController.class);

    GroupController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, UserRepository userRepository, GroupRepository groupRepository, GroupMessageRepository groupMessageRepository) {
        super(authenticationManager, jwtTokenUtil, userService, userRepository, groupRepository, groupMessageRepository);
    }

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
                                               @RequestBody GroupMessagesRequest groupMessagesRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupRepository.findPomodoroGroupByName(groupMessagesRequest.getGroupName()).get(0);
        Integer limit = groupMessagesRequest.getStop() - groupMessagesRequest.getStart();
        List<GroupMessage> groupMessages = groupMessageRepository.findLastMessagesByGroupIdWithinLimitAndOffset(group.getId(), limit, groupMessagesRequest.getStart());
        return ResponseEntity.ok(groupMessages);
    }

    @RequestMapping(value = "/group/create", method = RequestMethod.POST)
    public void createGroup(HttpServletRequest req, @RequestBody Group group) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        userService.createGroup(user, group.getName(), group.isPublic());
    }

    @RequestMapping(value = "/group/addUser", method = RequestMethod.POST)
    public ResponseEntity<?> addUserToTheGroup(HttpServletRequest req, @Valid @RequestBody GroupRequest groupRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Map<String, String> responseEntity = new HashMap<>();
        int status = 400;
        Group originalGroup = groupRepository.findPomodoroGroupByNameAndOwnerId(groupRequest.getGroupName(), user.getId());
        User userToAdd = userRepository.findUserByUsername(groupRequest.getUsername());
        basicGroupChecks(originalGroup, responseEntity, userToAdd);
        if (originalGroup != null && userToAdd != null && originalGroup.getUsers().stream().anyMatch(user1 -> user1.getUsername().equals(userToAdd.getUsername()))) {
            responseEntity.put("group", "User already is part of the group");
        }
        if (responseEntity.size() == 0) {
            status = 200;
            userService.addUserToGroup(originalGroup, userToAdd);
            responseEntity.put("success", String.format("User %s was successfully added to the group %s", groupRequest.getUsername(), originalGroup.getName()));
        }
        return ResponseEntity.status(status).body(responseEntity);
    }

    @RequestMapping(value = "/group/removeUser", method = RequestMethod.POST)
    public ResponseEntity<?> deleteUserFromTheGroup(HttpServletRequest req, @Valid @RequestBody GroupRequest groupRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Map<String, String> responseEntity = new HashMap<>();
        int status = 400;
        Group originalGroup = groupRepository.findPomodoroGroupByNameAndOwnerId(groupRequest.getGroupName(), user.getId());

        User userToRemove = userRepository.findUserByUsername(groupRequest.getUsername());
        basicGroupChecks(originalGroup, responseEntity, userToRemove);
        if (responseEntity.size() == 0) {
            status = 200;
            userService.removeUserFromGroup(originalGroup, userToRemove);
            responseEntity.put("success", String.format("User %s was successfully removed from the group %s", groupRequest.getUsername(), originalGroup.getName()));
        }
        return ResponseEntity.status(status).body(responseEntity);
    }

    private void basicGroupChecks(Group originalGroup, Map<String, String> responseEntity, User user) {
        if (originalGroup == null) {
            responseEntity.put("group", "Either you are not the owner of the group or group doest not exist");
        }
        if (user == null) {
            responseEntity.put("username", "Given user does not exist");
        }

    }
}
