package com.pomodoro.service.serviceimplementation;

import com.pomodoro.model.Group;
import com.pomodoro.model.GroupMessage;
import com.pomodoro.model.GroupToDo;
import com.pomodoro.model.User;
import com.pomodoro.service.IGroupService;
import com.pomodoro.service.IStorageService;
import com.pomodoro.utils.RequestDataNotValidException;
import com.pomodoro.utils.RequestError;
import com.pomodoro.utils.SizeUnit;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class LimitedGroupService implements IGroupService {

    private static final Integer USER_GROUP_LIMIT = 50;
    private static final Integer GROUP_LIMIT = 100;
    private static final Integer GROUP_TO_DO_LIMIT = 1000;
    private static final Integer GROUP_MESSAGE_LIMIT = 10000;
    private static final Integer GROUP_ATTACHMENTS_LIMIT_MG = 500;


    private final IGroupService groupService;
    private final IStorageService storageService;

    public LimitedGroupService(IGroupService groupService, IStorageService storageService) {
        this.groupService = groupService;
        this.storageService = storageService;
    }


    @Override
    public Group getGroup(User user, String groupName) {
        return user.getGroups()
                .stream()
                .filter(group -> groupName.equals(group.getName()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    @Override
    public Group createGroup(User author, String layoutImage, String name, boolean isPublic, String description) throws RequestDataNotValidException {
        List<RequestError> errors = new ArrayList<>();
        Set<Group> groups = author.getGroups();
        if (groups.size() >= GROUP_LIMIT) {
            errors.add(new RequestError("groupCountLimit", String.format("Group count limit exceeded. Maximum number of groups per user is :[%s]", GROUP_LIMIT)));
        } else if (groups.stream().anyMatch(group -> name.equals(group.getName()))) {
            errors.add(new RequestError("groupName", "Group with this name already exists"));
        }
        if (errors.size() > 0) {
            throw new RequestDataNotValidException(errors);
        }
        return groupService.createGroup(author, layoutImage, name, isPublic, description);
    }

    @Override
    public void addUserToGroup(User author, Group group, User userToAdd) throws RequestDataNotValidException {
        List<RequestError> errors = new ArrayList<>();
        Set<User> users = group.getUsers();
        if (group.getOwner().getId().equals(author.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else if (users.size() >= USER_GROUP_LIMIT) {
            errors.add(new RequestError("groupUsersCountLimit", String.format("User count limit for group [%s] exceeded. Maximum number of users per group is :[%s]", group.getName(), GROUP_LIMIT)));
        } else if (users.stream().anyMatch(user -> userToAdd.getId().equals(user.getId()))) {
            errors.add(new RequestError("user", "User is already in the group"));
        }
        if (errors.size() > 0) {
            throw new RequestDataNotValidException(errors);
        }
        groupService.addUserToGroup(author, group, userToAdd);
    }

    @Override
    public void removeUserFromGroup(User author, Group group, User userToRemove) throws RequestDataNotValidException {
        List<RequestError> errors = new ArrayList<>();
        Set<User> users = group.getUsers();
        if (group.getOwner().getId().equals(author.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else if (users.stream().noneMatch(user -> userToRemove.getId().equals(user.getId()))) {
            errors.add(new RequestError("user", "User is not in the group"));
        }
        if (errors.size() > 0) {
            throw new RequestDataNotValidException(errors);
        }
        groupService.removeUserFromGroup(author, group, userToRemove);
    }

    @Override
    public void addToDo(User author, Group group, GroupToDo newGroupToDo) throws RequestDataNotValidException {
        List<RequestError> errors = new ArrayList<>();
        List<GroupToDo> groupToDos = group.getGroupGroupToDos();
        if (groupToDos.size() >= GROUP_TO_DO_LIMIT) {
            errors.add(new RequestError("groupTodoCountLimit", String.format("Group to do count limit for group [%s] exceeded. Maximum number of to dos per group is :[%s]", group.getName(), GROUP_TO_DO_LIMIT)));
        } else if (groupToDos.stream().anyMatch(groupToDo -> newGroupToDo.getDescription().trim().equals(groupToDo.getDescription()))) {
            errors.add(new RequestError("groupToDo", "To do with this name is already in the group"));
        }
        if (errors.size() > 0) {
            throw new RequestDataNotValidException(errors);
        }
        groupService.addToDo(author, group, newGroupToDo);
    }

    @Override
    public void removeToDo(User author, Group group, List<GroupToDo> toDos) throws RequestDataNotValidException {
        List<RequestError> errors = new ArrayList<>();
        List<Integer> groupToDosIds = group.getGroupGroupToDos().stream().map(GroupToDo::getId).collect(Collectors.toList());
        if (toDos.stream().anyMatch(todo -> !todo.getAuthor().getId().equals(author.getId()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else if (toDos.stream().anyMatch(toDo -> !groupToDosIds.contains(toDo.getId()))) {
            errors.add(new RequestError("groupToDo", "Todo is not in the group"));
        }
        if (errors.size() > 0) {
            throw new RequestDataNotValidException(errors);
        }
        groupService.removeToDo(author, group, toDos);
    }

    @Override
    public void inviteUserToGroup(User author, Group group, User userToInvite) throws RequestDataNotValidException {
        List<RequestError> errors = new ArrayList<>();
        Set<User> users = group.getUsers();
        if (!group.getOwner().getId().equals(author.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else if (users.stream().anyMatch(user -> userToInvite.getId().equals(user.getId()))) {
            errors.add(new RequestError("user", "User is already in the group"));
        }
        if (errors.size() > 0) {
            throw new RequestDataNotValidException(errors);
        }
        groupService.inviteUserToGroup(author, group, userToInvite);
    }

    @Override
    public GroupMessage createGroupMessage(User author, Group group, String value) throws RequestDataNotValidException {
        List<RequestError> errors = new ArrayList<>();
        Set<User> users = group.getUsers();
        List<GroupMessage> groupMessages = group.getGroupMessages();
        if (groupMessages.size() >= GROUP_MESSAGE_LIMIT) {
            errors.add(new RequestError("group", String.format("Group messages count limit for group [%s] exceeded. Maximum number of messages per group is :[%s]", group.getName(), GROUP_MESSAGE_LIMIT)));
        } else if (users.stream().noneMatch(user -> user.getId().equals(author.getId()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (errors.size() > 0) {
            throw new RequestDataNotValidException(errors);
        }
        return groupService.createGroupMessage(author, group, value);
    }

    @Override
    public GroupMessage createGroupMessageAttachment(User author, Group group, MultipartFile file) throws RequestDataNotValidException {
        List<RequestError> errors = new ArrayList<>();
        SizeUnit unit = SizeUnit.MB;
        long size = storageService.getGroupAttachmentsSize(group, unit);
        size+=(file.getSize()/unit.getInByte());
        System.out.println(String.format("%s attachments size is %s %s", group.getName(), String.valueOf(size), unit.toString()));
        ;
        if (size > GROUP_ATTACHMENTS_LIMIT_MG) {
            errors.add(new RequestError("group", String.format("Attachments size for group [%s] exceeded. Maximum size of attachments is :[%s] MG", group.getName(), GROUP_ATTACHMENTS_LIMIT_MG)));
        }
        if (errors.size() > 0) {
            throw new RequestDataNotValidException(errors);
        }
        return groupService.createGroupMessageAttachment(author, group, file);
    }

    @Override
    public void setGroupReaction(User author, Group group, String reaction) throws RequestDataNotValidException {
        Set<User> users = group.getUsers();
        if (users.stream().noneMatch(user -> user.getId().equals(author.getId()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        groupService.setGroupReaction(author, group, reaction);
    }

    @Override
    public void updateGroupMessage(User author, GroupMessage groupMessage, GroupMessage updatedMessage) throws RequestDataNotValidException {
        if (groupMessage.getAuthor().getId().equals(author.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        groupService.updateGroupMessage(author, groupMessage, updatedMessage);
    }


}
