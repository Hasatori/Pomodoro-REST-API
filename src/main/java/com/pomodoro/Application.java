package com.pomodoro;

import com.pomodoro.model.ChangeType;
import com.pomodoro.model.change.MessageChange;
import com.pomodoro.model.change.ToDoChange;
import com.pomodoro.model.group.Group;
import com.pomodoro.model.group.GroupInvitation;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.reaction.UserReaction;
import com.pomodoro.model.todo.GroupToDo;
import com.pomodoro.model.todo.UserToDo;
import com.pomodoro.model.user.Settings;
import com.pomodoro.model.user.User;
import com.pomodoro.service.StorageProperties;
import com.pomodoro.service.repository.*;
import com.pomodoro.utils.DateUtils;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.nio.charset.Charset;
import java.time.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
    public ApplicationRunner initializer
            (
                    UserRepository userRepository,
                    SettingsRepository settingsRepository,
                    GroupRepository groupRepository,
                    GroupMessageRepository groupMessageRepository,
                    UserReactionRepository userReactionRepository,
                    GroupInvitationRepository groupInvitationRepository,
                    GroupTodoRepository groupTodoRepository,
                    UserTodoRepository userTodoRepository
            ) {
        return args -> {
            User hasatori = userRepository.save(getBasicUser("Hasatori", "hradil.o@email.cz"));
            User test1 = userRepository.save(getBasicUser("Test1", "hradil.o1@email.cz"));
            User test2 = userRepository.save(getBasicUser("Test2", "hradil.o2@email.cz"));
            User test3 = userRepository.save(getBasicUser("Test3", "hradil.o3@email.cz"));
            User test4 = userRepository.save(getBasicUser("Test4", "hradil.o4@email.cz"));

            List<User> allUsers = userRepository.findAll();
            allUsers.forEach(user -> {
                settingsRepository.save(getDefaultSettings(user));
                addSomeTodosToUser(user, userTodoRepository);
            });
            Group rodina = groupRepository.save(getGroup(hasatori, "Rodina", new HashSet<>(allUsers)));
            addMessagesToGroup(rodina, groupMessageRepository, userReactionRepository);
            addSomeTodosToGroup(rodina, groupTodoRepository);
            for (int i = 0; i < 5; i++) {
                List<User> groupMembers = new ArrayList<>();
                groupMembers.add(hasatori);
                groupMembers.addAll(getNumberOfRandomUsers(allUsers, hasatori, 2));
                Group group = groupRepository.save(getGroup(hasatori, "Test" + i, new HashSet<>(groupMembers)));
                addMessagesToGroup(group, groupMessageRepository, userReactionRepository);
                addSomeTodosToGroup(group, groupTodoRepository);
            }
            List<Group> filteredGroups = groupRepository.findAll().stream().filter(group -> !"Rodina".equals(group.getName())).collect(Collectors.toList());
            filteredGroups.forEach(group -> {
                getUsersNotMembersOfAGroup(allUsers, group).forEach(user -> {
                    groupInvitationRepository.save(getGroupInvitation(group, user));
                });
            });
        };
    }

    private static void addMessagesToGroup(Group group, GroupMessageRepository groupMessageRepository, UserReactionRepository userReactionRepository) {
        List<User> groupMembers = new ArrayList<>(group.getUsers());
        for (int j = 0; j < 1000; j++) {
            User messageAuthor = groupMembers.get(random.nextInt(groupMembers.size()));
            List<User> usersForReaction = getNumberOfRandomUsers(groupMembers, messageAuthor, random.nextInt(groupMembers.size()));
            GroupMessage groupMessage = groupMessageRepository.save(getRandomGroupMessage(messageAuthor, group, null));
            usersForReaction.forEach(user -> {
                userReactionRepository.save(getGroupMessageReaction(user, groupMessage));
                if (random.nextBoolean()) {
                    groupMessageRepository.save(getRandomGroupMessage(user, group, groupMessage));
                }
            });
        }
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
        group.setCreated(getRandomDate());
        group.setDescription("Group description");
        group.setLayoutImage(getRandomLayoutImage());
        group.setIsPublic(false);
        group.setOwner(owner);
        group.setOwnerId(owner.getId());
        group.setUsers(users);
        return group;
    }

    private static String getRandomLayoutImage() {
        int number = random.nextInt(5);
        return String.format("group/layout/teamwork-%d.jpg", ++number);
    }

    private static GroupMessage getRandomGroupMessage(User author, Group group, GroupMessage parent) {
        GroupMessage groupMessage = new GroupMessage();
        byte[] array = new byte[7]; // length is bounded by 7
        random.nextBytes(array);
        groupMessage.setValue(new String(array, Charset.forName("UTF-8")));
        groupMessage.setGroup(group);
        groupMessage.setGroupId(group.getId());
        groupMessage.setAuthor(author);
        groupMessage.setAuthorId(author.getId());
        if (parent != null) {
            groupMessage.setParentId(parent.getId());
            groupMessage.setParent(parent);
        }
        groupMessage.setCreationTimestamp(getRandomDate());
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

    private static GroupInvitation getGroupInvitation(Group group, User user) {
        GroupInvitation groupInvitation = new GroupInvitation();
        groupInvitation.setAccepted(false);
        groupInvitation.setRefused(false);
        groupInvitation.setGroup(group);
        groupInvitation.setGroupId(group.getId());
        groupInvitation.setInvitedUser(user);
        groupInvitation.setInvitedUserId(user.getId());
        return groupInvitation;
    }

    private static List<User> getUsersNotMembersOfAGroup(List<User> allUsers, Group group) {
        return allUsers
                .stream()
                .filter(user -> group.getUsers().stream().noneMatch(user1 -> user.getUsername().equals(user1.getUsername())))
                .collect(Collectors.toList());
    }

    public static Instant between(Instant startInclusive, Instant endExclusive) {
        long startSeconds = startInclusive.getEpochSecond();
        long endSeconds = endExclusive.getEpochSecond();
        long random = ThreadLocalRandom
                .current()
                .nextLong(startSeconds, endSeconds);

        return Instant.ofEpochSecond(random);
    }

    public static LocalDateTime getRandomDate() {
        Instant hundredYearsAgo = Instant.now().minus(Duration.ofDays(100 * 365));
        Instant tenDaysAgo = Instant.now().minus(Duration.ofDays(10));
        return LocalDateTime.ofInstant(between(hundredYearsAgo, tenDaysAgo), ZoneId.systemDefault());
    }


    private static void addSomeTodosToGroup(Group group, GroupTodoRepository groupTodoRepository) {
        int numberOfTasks = random.nextInt(10);
        for (int i = 0; i < numberOfTasks; i++) {
            int depth = random.nextInt(5);
            int currentDepth = 0;
            String currentDepthToDoName="Test" + i;
            GroupToDo currentDepthToDo = groupTodoRepository.save(getGroupToDo(group.getOwner(), group, currentDepthToDoName, null));
            while (currentDepth != depth) {
                currentDepthToDoName+=".1";
                currentDepthToDo = groupTodoRepository.save(getGroupToDo(group.getOwner(), group, currentDepthToDoName, currentDepthToDo));
                currentDepth++;
            }
        }
    }

    private static void addSomeTodosToUser(User user, UserTodoRepository userTodoRepository) {
        int numberOfTasks = random.nextInt(10);
        for (int i = 0; i < numberOfTasks; i++) {
            int depth = random.nextInt(5);
            int currentDepth = 0;
            String currentDepthToDoName="Test" + i;
            UserToDo currentDepthToDo = userTodoRepository.save(getUserToDo(user, currentDepthToDoName, null));
            while (currentDepth != depth) {
                currentDepthToDoName+=".1";
                currentDepthToDo = userTodoRepository.save(getUserToDo(user, currentDepthToDoName, currentDepthToDo));
                currentDepth++;
            }
        }
    }

    private static UserToDo getUserToDo(User user, String name, UserToDo parent) {
        UserToDo userToDo = new UserToDo();
        userToDo.setAuthor(user);
        userToDo.setAuthorId(user.getId());
        userToDo.setName(name);
        userToDo.setDescription("Task testing description");
        userToDo.setDeadline(getRandomDate());
        if (parent != null) {
            userToDo.setParent(parent);
            userToDo.setParentId(parent.getId());
        }
        return userToDo;
    }

    private static GroupToDo getGroupToDo(User author, Group group, String name, GroupToDo parent) {
        GroupToDo groupToDo = new GroupToDo();
        groupToDo.setGroup(group);
        groupToDo.setGroupId(group.getId());
        groupToDo.setAuthor(author);
        groupToDo.setAuthorId(author.getId());
        if (parent != null) {
            groupToDo.setParent(parent);
            groupToDo.setParentId(parent.getId());
        }
        groupToDo.setName(name);
        groupToDo.setDescription("Task testing description");
        groupToDo.setDeadline(getRandomDate());
        return groupToDo;
    }
}
