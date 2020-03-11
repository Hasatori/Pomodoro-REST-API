package com.pomodoro.service;

import com.pomodoro.model.Group;
import com.pomodoro.model.GroupMessage;
import com.pomodoro.model.GroupToDo;
import com.pomodoro.model.User;
import com.pomodoro.utils.RequestDataNotValidException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public interface IGroupService {


    Group getGroup(User author, String groupName);


    Group createGroup(User author, String layoutImage, String name, boolean isPublic,String description) throws RequestDataNotValidException;

    void addUserToGroup(User author, Group group, User userToAdd) throws RequestDataNotValidException;

    void removeUserFromGroup(User author, Group group, User userToRemove) throws RequestDataNotValidException;

    void addToDo(User author, Group group, GroupToDo groupToDo) throws RequestDataNotValidException;

    void removeToDo(User author, Group group, List<GroupToDo> toDos) throws RequestDataNotValidException;

    void inviteUserToGroup(User author, Group group, User userToInvite) throws RequestDataNotValidException;

    GroupMessage createGroupMessage(User author, Group group, String value) throws RequestDataNotValidException;

    GroupMessage createGroupMessageAttachment(User author, Group group, MultipartFile file) throws RequestDataNotValidException;

    void setGroupReaction(User author, Group group, String reaction) throws RequestDataNotValidException;

    void updateGroupMessage(User author, GroupMessage groupMessage,GroupMessage updatedMessage) throws RequestDataNotValidException;


}
