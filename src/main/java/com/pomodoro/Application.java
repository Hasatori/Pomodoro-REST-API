package com.pomodoro;

import com.pomodoro.model.group.Group;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.reaction.UserReaction;
import com.pomodoro.model.user.Settings;
import com.pomodoro.model.user.User;
import com.pomodoro.service.StorageProperties;
import com.pomodoro.service.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

    private static Random random = new Random();

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);

    }

    @Bean
    public ApplicationRunner initializer(UserRepository userRepository, SettingsRepository settingsRepository, GroupRepository groupRepository, GroupMessageRepository groupMessageRepository, UserReactionRepository userReactionRepository) {
        return args -> {
            User hasatori = userRepository.save(getBasicUser("Hasatori", "hradil.o@email.cz"));
            User test1 = userRepository.save(getBasicUser("Test1", "hradil.o1@email.cz"));
            User test2 = userRepository.save(getBasicUser("Test2", "hradil.o2@email.cz"));
            User test3 = userRepository.save(getBasicUser("Test3", "hradil.o3@email.cz"));
            User test4 = userRepository.save(getBasicUser("Test4", "hradil.o4@email.cz"));

            List<User> allUsers = userRepository.findAll();
            allUsers.forEach(user -> settingsRepository.save(getDefaultSettings(user)));
            groupRepository.save(getGroup(hasatori, "Rodina", new HashSet<>(allUsers)));
            for (int i = 0; i < 5; i++) {

                List<User> groupMembers = new ArrayList<>();
                groupMembers.add(hasatori);
                groupMembers.addAll(getNumberOfRandomUsers(allUsers, hasatori, 3));
                Group group = groupRepository.save(getGroup(hasatori, "Test" + i, new HashSet<>(groupMembers)));
                for (int j = 0; j < 1000; j++) {
                    User messageAuthor = groupMembers.get(random.nextInt(groupMembers.size()));
                    List<User> usersForReaction = getNumberOfRandomUsers(groupMembers, messageAuthor, random.nextInt(groupMembers.size()));
                    GroupMessage groupMessage =   groupMessageRepository.save(getRandomGroupMessage(messageAuthor,group));
                    usersForReaction.forEach(user -> userReactionRepository.save(getGroupMessageReaction(user, groupMessage)));
                }
            }
        };
    }

    private static User getBasicUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName("Oldřich");
        user.setLastName("Hradil");
        user.setPassword("$2a$10$JSqrJ4lIOCTyf1/GXSYiVOoyyiR.fVemo9EMU1qDkIzxZhyI4XrIa");
        user.setAccountExpired(false);
        user.setLocked(false);
        user.setCredentialsExpired(false);
        user.setEnabled(true);
        user.setFacebookId(null);
        return user;
    }

    private static Settings getDefaultSettings(User user) {
        Settings settings = new Settings();
        settings.setWorkTime(1500);
        settings.setPauseTime(300);
        settings.setUser(user);
        settings.setId(user.getId());
        return settings;
    }

    private static Group getGroup(User owner, String name, Set<User> users) {
        Group group = new Group();
        group.setName(name);
        group.setCreated(new Date());
        group.setDescription("Group description");
        group.setLayoutImage(getRandomLayoutImage());
        group.setIsPublic(false);
        group.setOwner(owner);
        group.setOwnerId(owner.getId());
        group.setUsers(users);
        return group;
    }

    private static String getRandomLayoutImage() {
        Integer number = random.nextInt(6);
        return String.format("group/layout/teamwork-%d.jpg", number);
    }

    private static GroupMessage getRandomGroupMessage(User author, Group group) {
        GroupMessage groupMessage = new GroupMessage();
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        groupMessage.setValue(new String(array, Charset.forName("UTF-8")));
        groupMessage.setGroup(group);
        groupMessage.setGroupId(group.getId());
        groupMessage.setAuthor(author);
        groupMessage.setAuthorId(author.getId());
        return groupMessage;
    }

    private static List<User> getNumberOfRandomUsers(List<User> allUsers, User owner, int numberOfUsersToAdd) {
        List<User> result = new ArrayList<>();
        if (numberOfUsersToAdd > allUsers.size() - 1) {
            throw new IllegalStateException("Number of users to add is higher that number of all users");
        }
        allUsers = allUsers.stream().filter(user -> !user.getUsername().equals(owner.getUsername())).collect(Collectors.toList());
        while (result.size() != numberOfUsersToAdd) {
            User memberCandidate = allUsers.get(random.nextInt(allUsers.size()));
            while (isUserInCollection(result, memberCandidate)) {
                memberCandidate = allUsers.get(random.nextInt(allUsers.size()));
            }
            result.add(memberCandidate);
        }
        return result;
    }

    private static boolean isUserInCollection(List<User> collection, User userToVerify) {
        return collection.stream().anyMatch(user -> user.getUsername().equals(userToVerify.getUsername()));
    }

    private static UserReaction getGroupMessageReaction(User author, GroupMessage groupMessage) {
        UserReaction reaction = new UserReaction();
        String[] reactions = {"happy", "crying", "angry"};
        reaction.setMessageId(groupMessage.getId());
        reaction.setAuthorId(author.getId());
        reaction.setReadTimestamp(null);
        reaction.setReaction(reactions[random.nextInt(reactions.length)]);
        return reaction;
    }
}
