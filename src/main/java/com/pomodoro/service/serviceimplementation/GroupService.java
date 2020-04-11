package com.pomodoro.service.serviceimplementation;


import com.pomodoro.model.group.Group;
import com.pomodoro.model.group.GroupInvitation;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.reaction.UserReaction;
import com.pomodoro.model.todo.GroupToDo;
import com.pomodoro.model.user.User;
import com.pomodoro.service.IGroupService;
import com.pomodoro.service.IStorageService;
import com.pomodoro.service.repository.GroupInvitationRepository;
import com.pomodoro.service.repository.GroupMessageRepository;
import com.pomodoro.service.repository.GroupRepository;
import com.pomodoro.service.repository.GroupTodoRepository;
import com.pomodoro.utils.DateUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("basicGroupService")
class GroupService implements IGroupService {

    private final GroupRepository groupRepository;
    private final GroupInvitationRepository groupInvitationRepository;
    private final GroupTodoRepository groupTodoRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final IStorageService storageService;

    GroupService(GroupRepository groupRepository, GroupInvitationRepository groupInvitationRepository, GroupTodoRepository groupTodoRepository, GroupMessageRepository groupMessageRepository, @Qualifier("basicStorageService") IStorageService storageService) {
        this.groupRepository = groupRepository;
        this.groupInvitationRepository = groupInvitationRepository;
        this.groupTodoRepository = groupTodoRepository;
        this.groupMessageRepository = groupMessageRepository;
        this.storageService = storageService;
    }


    @Override
    public Group getGroup(User author, String groupName) {
        return author.getMemberOfGroups()
                .stream()
                .filter(group -> groupName.equals(group.getName()))
                .findFirst().get();

    }

    @Override
    public Group createGroup(User author, String layoutImage, String name, boolean isPublic, String description) {
        Group group = new Group();
        group.setLayoutImage(layoutImage);
        group.setName(name.trim());
        group.setOwnerId(author.getId());
        group.setIsPublic(isPublic);
        group.setDescription(description);
        group.setCreated(DateUtils.getCurrentLocalDateTimeUtc());
        group.setUsers(new HashSet<User>() {{
            add(author);
        }});
       return groupRepository.save(group);
    }

    @Override
    public void addUserToGroup(User author, Group group, User userToAdd) {
        group.getUsers().add(userToAdd);
        groupRepository.save(group);
    }

    @Override
    public void removeUserFromGroup(User author, Group group, User userToRemove) {
        group.getUsers().removeIf(user -> user.getId().equals(userToRemove.getId()));
        groupRepository.save(group);
    }

    @Override
    public void addToDo(User author, Group group, GroupToDo groupToDo) {
        groupTodoRepository.save(groupToDo);
    }

    @Override
    public void removeToDo(User author, Group group, List<GroupToDo> toDos) {
        groupTodoRepository.deleteGroupTodos(toDos.stream().map(GroupToDo::getId).collect(Collectors.toList()));
    }

    @Override
    public void inviteUserToGroup(User author, Group group, User userToInvite) {
        GroupInvitation groupInvitation = new GroupInvitation();
        groupInvitation.setGroupId(group.getId());
        groupInvitation.setInvitedUserId(userToInvite.getId());
        groupInvitation.setAccepted(false);
        groupInvitationRepository.save(groupInvitation);
    }

    @Override
    public GroupMessage createGroupMessage(User author, Group group, String value) {
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setAuthor(author);
        groupMessage.setAuthorId(author.getId());
        groupMessage.setValue(value);
        groupMessage.setCreationTimestamp(LocalDateTime.now());
        groupMessage.setGroup(group);
        groupMessage.setGroupId(group.getId());
        groupMessage.setReactions(new ArrayList<>());
        groupMessage = groupMessageRepository.save(groupMessage);
        for (User user : groupMessage.getGroup().getUsers()) {
            UserReaction groupMessageReaction = new UserReaction();
            groupMessageReaction.setAuthor(user);
            if (user.getUsername().equals(author.getUsername())) {
                groupMessageReaction.setReadTimestamp(DateUtils.getCurrentLocalDateTimeUtc());
            }

            groupMessageReaction.setMessage(groupMessage);

            groupMessage.getReactions().add(groupMessageReaction);
        }
       return groupMessageRepository.save(groupMessage);
    }

    @Override
    public GroupMessage createGroupMessageAttachment(User author, Group group, MultipartFile file) {
      //  List<String> groupAttachments = group.getGroupMessages().stream().map(GroupMessage::getAttachment).collect(Collectors.toList());
        List<String> groupAttachments=new ArrayList<>();
        UUID uniqueKey = UUID.randomUUID();
        while (groupAttachments.contains(uniqueKey.toString())) {
            uniqueKey = UUID.randomUUID();
        }
        String attachmentValue = String.format("%s.%s", uniqueKey.toString(), FilenameUtils.getExtension(file.getResource().getFilename()));
        storageService.store(file, String.format("group/%d/attachment/%s", group.getId(), attachmentValue));
        GroupMessage groupMessage=createGroupMessage(author,group,file.getResource().getFilename());
        return groupMessageRepository.save(groupMessage);
    }

    @Override
    public void setGroupReaction(User author, Group group, String reaction) {

    }

    @Override
    public void updateGroupMessage(User author, GroupMessage groupMessage, GroupMessage updatedMessage) {

    }
}
