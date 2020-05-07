package com.pomodoro.controller;

import com.pomodoro.model.change.GroupChange;
import com.pomodoro.model.group.Group;
import com.pomodoro.model.group.GroupInvitation;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.request.*;
import com.pomodoro.model.todo.GroupToDo;
import com.pomodoro.model.user.User;
import com.pomodoro.utils.DateUtils;
import com.pomodoro.utils.RequestDataNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public Set<Group> getGroups(HttpServletRequest req) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return user.getMemberOfGroups();
    }

    @RequestMapping(value = "/groups/{groupName}", method = RequestMethod.GET)
    public Set<User> getUsersForGroup(HttpServletRequest req, @PathVariable(required = true) String groupName) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return groupService.getGroup(user, groupName).getUsers();
    }

    @RequestMapping(value = "/groups/fetch-chat-messages", method = RequestMethod.POST)
    public List<GroupMessage> fetchChatMessages(HttpServletRequest req,
                                                @RequestBody GroupDataRequest groupDataRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupService.getGroup(user, groupDataRequest.getGroupName());
        Integer limit = groupDataRequest.getStop() - groupDataRequest.getStart();
        return groupMessageRepository.findLastMessagesByGroupIdWithinLimitAndOffset(group.getId(), limit, groupDataRequest.getStart());
    }

    @RequestMapping(value = "/groups/{groupName}/fetch-unread-messages", method = RequestMethod.GET)
    public List<GroupMessage> fetchUnreadMessages(HttpServletRequest req,@PathVariable(required = true) String groupName) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupService.getGroup(user, groupName);
        return groupMessageRepository.findAllUnreadMessages(user.getId(), group.getId());
    }

    @RequestMapping(value = "/groups/{groupName}/mark-all-as-read", method = RequestMethod.POST)
    public void markAllAsRead(HttpServletRequest req, @PathVariable(required = true) String groupName) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupService.getGroup(user, groupName);
        userReactionRepository.markAllUserMessagesFromGroupAsRead(DateUtils.getCurrentLocalDateTimeUtc(), user.getId(), group.getGroupMessages().stream().map(GroupMessage::getId).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/groups/fetch-changes", method = RequestMethod.POST)
    public List<GroupChange> fetchGroupChanges(HttpServletRequest req, @RequestBody GroupDataRequest groupDataRequest) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupService.getGroup(user, groupDataRequest.getGroupName());
        Integer limit = groupDataRequest.getStop() - groupDataRequest.getStart();
        return groupChangeRepository.findLastChangesByGroupIdWithinLimitAndOffset(group.getId(), limit, groupDataRequest.getStart());
    }

    @RequestMapping(value = "/groups/{groupName}/fetch-todos", method = RequestMethod.GET)
    public List<GroupToDo> fetchGroupTodos(HttpServletRequest req, @PathVariable(required = true) String groupName) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupService.getGroup(user, groupName);
        return group.getGroupTodos();
    }

    @RequestMapping(value = "/group/create", method = RequestMethod.POST)
    public void createGroup(HttpServletRequest req, @RequestBody @Valid GroupCreationRequest groupCreationRequest) throws RequestDataNotValidException {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        groupService.createGroup(user, groupCreationRequest.getLayoutImage(), groupCreationRequest.getGroupName(), groupCreationRequest.getIsPublic(),groupCreationRequest.getDescription());
    }

    @RequestMapping(value = "/group/removeUser", method = RequestMethod.POST)
    public void deleteUserFromTheGroup(HttpServletRequest req, @Valid @RequestBody GroupUserRequest groupUserRequest) throws RequestDataNotValidException {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        User userToRemove = userRepository.findUserByUsername(groupUserRequest.getUsername());
        groupService.removeUserFromGroup(user, groupService.getGroup(user, groupUserRequest.getGroupName()), userToRemove);
    }

    @RequestMapping(value = "/group/invite-user", method = RequestMethod.POST)
    public void inviteUserToGroup(HttpServletRequest req, @Valid @RequestBody GroupInvitationRequest groupInvitationRequest) throws RequestDataNotValidException {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        groupService.inviteUserToGroup(user, groupInvitationRequest.getGroup(), groupInvitationRequest.getInvitedUser());
    }

    @RequestMapping(value = "/group/{groupName}/invitations", method = RequestMethod.GET)
    public List<GroupInvitation> getGroupInvitations(HttpServletRequest req, @PathVariable String groupName) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupService.getGroup(user, groupName);
        return group.getGroupInvitations();
    }

    @RequestMapping(value = "/group/remove-toDoId", method = RequestMethod.POST)
    public void deleteToDoFromTheGroup(HttpServletRequest req, @Valid @RequestBody GroupTodoRequest groupTodoRequest) throws RequestDataNotValidException {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group=groupService.getGroup(user,groupTodoRequest.getGroup().getName());
        groupService.removeToDo(user,group, groupTodoRequest.getToDoList());
    }
}
