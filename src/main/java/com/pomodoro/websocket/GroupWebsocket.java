package com.pomodoro.websocket;

import com.pomodoro.model.Group;
import com.pomodoro.model.GroupMessage;
import com.pomodoro.model.User;
import com.pomodoro.model.UserGroupMessage;
import com.pomodoro.repository.GroupMessageRepository;
import com.pomodoro.repository.GroupRepository;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GroupWebsocket extends AbstractSocket {

    GroupWebsocket(UserService userService, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository, GroupMessageRepository groupMessageRepository, GroupRepository groupRepository) {
        super(userService, simpMessagingTemplate, userRepository, groupMessageRepository, groupRepository);
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
}
