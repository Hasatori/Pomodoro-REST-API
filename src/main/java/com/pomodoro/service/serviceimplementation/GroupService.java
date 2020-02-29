package com.pomodoro.service.serviceimplementation;

import com.pomodoro.model.*;
import com.pomodoro.service.IGroupService;
import com.pomodoro.service.repository.GroupInvitationRepository;
import com.pomodoro.service.repository.GroupRepository;
import com.pomodoro.service.repository.GroupTodoRepository;
import com.pomodoro.utils.DateUtils;
import com.pomodoro.utils.RequestDataNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("basicGroupService")
class GroupService implements IGroupService {

    private final GroupRepository groupRepository;
    private final GroupInvitationRepository groupInvitationRepository;
    private final GroupTodoRepository groupTodoRepository;

    GroupService(GroupRepository groupRepository, GroupInvitationRepository groupInvitationRepository, GroupTodoRepository groupTodoRepository) {
        this.groupRepository = groupRepository;
        this.groupInvitationRepository = groupInvitationRepository;
        this.groupTodoRepository = groupTodoRepository;
    }


    @Override
    public Group getGroup(User author, String groupName) {
        return author.getGroups()
                .stream()
                .filter(group -> groupName.equals(group.getName()))
                .findFirst().get();

    }

    @Override
    public void createGroup(User author, String layoutImage, String name, boolean isPublic,String description) {
        Group group = new Group();
        group.setLayoutImage(layoutImage);
        group.setName(name.trim());
        group.setOwnerId(author.getId());
        group.setIsPublic(isPublic);
        group.setDescription(description);
        group.setCreated(DateUtils.getCurrentDateUtc());
        group.setUsers(new HashSet<User>() {{
            add(author);
        }});
        groupRepository.save(group);
    }

    @Override
    public void addUserToGroup(User author, Group group, User userToAdd) {

    }

    @Override
    public void removeUserFromGroup(User author, Group group, User userToRemove) throws RequestDataNotValidException {

    }

    @Override
    public void addToDo(User author, Group group, GroupToDo groupToDo) throws RequestDataNotValidException {

    }

    @Override
    public void removeToDo(User author, Group group, List<GroupToDo> toDos) throws RequestDataNotValidException {
        groupTodoRepository.deleteGroupTodos(toDos.stream().map(GroupToDo::getId).collect(Collectors.toList()));
    }

    @Override
    public void inviteUserToGroup(User author, Group group, User userToInvite) throws RequestDataNotValidException {
        GroupInvitation groupInvitation = new GroupInvitation();
        groupInvitation.setGroupId(group.getId());
        groupInvitation.setInvitedUserId(userToInvite.getId());
        groupInvitation.setAccepted(false);
        groupInvitationRepository.save(groupInvitation);
    }

    @Override
    public void addGroupMessage(User author, Group group, String value) throws RequestDataNotValidException {

    }

    @Override
    public void addGroupMessageAttachment(User author, Group group, MultipartFile file) throws RequestDataNotValidException {

    }

    @Override
    public void setGroupReaction(User author, Group group, String reaction) throws RequestDataNotValidException {

    }

    @Override
    public void updateGroupMessage(User author, GroupMessage groupMessage, GroupMessage updatedMessage) throws RequestDataNotValidException {

    }
}
