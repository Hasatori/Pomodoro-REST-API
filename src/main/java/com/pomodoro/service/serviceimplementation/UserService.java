package com.pomodoro.service.serviceimplementation;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.model.*;
import com.pomodoro.model.o2auth.SecretStore;
import com.pomodoro.model.request.UpdateUserDetails;
import com.pomodoro.service.repository.GroupRepository;
import com.pomodoro.service.repository.PomodoroRepository;
import com.pomodoro.service.repository.SettingsRepository;
import com.pomodoro.service.repository.UserRepository;
import com.pomodoro.service.IUserService;
import com.pomodoro.utils.DateUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("basicUserService")
public class UserService implements UserDetailsService, IUserService {

    private final UserRepository userRepository;
    private final SettingsRepository settingsRepository;
    private final PomodoroRepository pomodoroRepository;
    private final GroupRepository groupRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtTokenUtil jwtTokenUtil;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, SettingsRepository settingsRepository, PomodoroRepository pomodoroRepository, GroupRepository groupRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
        this.pomodoroRepository = pomodoroRepository;
        this.groupRepository = groupRepository;
        this.jwtTokenUtil = jwtTokenUtil;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findUserByUsername(username);
        if (foundUser != null) {
            return foundUser;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest req) {
        String jwtToken = null;
        final String requestTokenHeader = req.getHeader("Authorization");
        final String socketHeader = req.getHeader("Sec-WebSocket-Key");
        final String socketToken = req.getParameter("token");
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
        } else if (socketHeader != null && socketToken != null) {
            jwtToken = socketToken.substring(7);
        }
        return jwtToken;
    }

    @Override
    public User getUserFromToken(String jwtToken) {
        String username = null;
        try {
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Token has expired");
        }
        return userRepository.findUserByUsername(username);
    }

    public void updateUser(User currentUser, UpdateUserDetails updatedUser) {
        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setEmail(updatedUser.getEmail());
        userRepository.save(currentUser);
    }

    public void updateUserSettings(User currentUser, Settings updatedSettings) {
        settingsRepository.updateUserSettings(currentUser.getId(), updatedSettings.getWorkTime(), updatedSettings.getPauseTime(), updatedSettings.getPhaseChangedSound(), updatedSettings.getWorkSound(), updatedSettings.getPauseSound());
    }

    public boolean passwordBelongsToTheUser(User user, String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, user.getPassword());
    }

    public void changePassword(User user, String oldPassword, String newPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPassword, user.getPassword())) {
            userRepository.updatePassword(user.getId(), encoder.encode(newPassword));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Pomodoro createPomodoroAndReturn(User user) {
        Pomodoro pomodoro = new Pomodoro();
        pomodoro.setUser(user.getId());
        pomodoro.setCreationTimestamp(DateUtils.getCurrentDateUtc());
        pomodoro.setWorkTime(user.getSettings().getWorkTime());
        pomodoro.setBreakTime(user.getSettings().getPauseTime());
        pomodoro.setInterrupted(false);
        return pomodoroRepository.save(pomodoro);
    }

    public Pomodoro getLastPomodoro(User user) {
        List<Pomodoro> pomodoros = user.getPomodoros();
        if (!pomodoros.isEmpty()) {
            Pomodoro pomodoro = Collections.max(user.getPomodoros(), Comparator.comparing(Pomodoro::getCreationTimestamp));
            return pomodoro;
        }
        return null;
    }

    public void stopPomodoro(User user, Pomodoro pomodoro) {
        pomodoroRepository.stopPomodoro(user.getId(), pomodoro.getCreationTimestamp());
    }

    public Set<User> getUsersForGroup(String groupName, User user) {
        List<Group> groups = groupRepository.findPomodoroGroupByName(groupName);
        Optional<Group> groupWithUsers = groups.stream().filter(group -> group.getUsers().stream().anyMatch(user1 -> user1.getUsername().equals(user.getUsername()))).findFirst();
        if (groupWithUsers.isPresent()) {
            return groupWithUsers.get().getUsers();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public Pomodoro getLastPomodoroForUser(String username) {
        User user = userRepository.findUserByUsername(username);
        List<Pomodoro> pomodoros = user.getPomodoros();
        if (!pomodoros.isEmpty()) {
            Pomodoro pomodoro = Collections.max(user.getPomodoros(), Comparator.comparing(Pomodoro::getCreationTimestamp));
            return pomodoro;
        }
        return null;
    }

    public void createGroup(User user, String name, boolean isPublic) {
        groupRepository.insertGroup(name, user.getId(), isPublic);
        Group group = groupRepository.findPomodoroGroupByNameAndOwnerId(name, user.getId());
        group.getUsers().add(user);
        groupRepository.save(group);
    }

    public void addUserToGroup(Group group, User userToAdd) {
        group.getUsers().add(userToAdd);
        groupRepository.save(group);
    }

    public void removeUserFromGroup(Group group, User userToRemove) {
        group.getUsers().removeIf(user -> user.getUsername().equals(userToRemove.getUsername()));
        groupRepository.save(group);
    }

    public void deleteUserFromGroup(User owner, String groupName, String userToDeleteName) {
        Group group = groupRepository.findPomodoroGroupByNameAndOwnerId(groupName, owner.getId());
        Optional<User> userToDeleteOptional = group
                .getUsers()
                .stream()
                .filter(user -> userToDeleteName.equals(user.getUsername()))
                .findFirst();
        if (userToDeleteOptional.isPresent()) {
            group.getUsers().remove(userToDeleteOptional.get());
            groupRepository.save(group);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public void registerNewUser(RegisterUser newUser) {
        newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        userRepository.insertNewUser(newUser.getUsername(), newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), newUser.getPassword(), false, false, false, true);
        settingsRepository.insertNewSettings(userRepository.findUserByUsername(newUser.getUsername()).getId(), 1500, 300, "Simple-alert-bells-tone.mp3", null, null);
    }

    public boolean facebookAccessTokenValid(String inputToken, String userId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://graph.facebook.com/debug_token")
                    .queryParam("input_token", inputToken)
                    .queryParam("access_token", SecretStore.FACEBOOK_SECRET);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<?> entity = new HttpEntity<>(headers);
            HttpEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);
            if (response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getFactory();
                JsonParser parser = null;

                parser = factory.createParser(response.getBody());

                JsonNode actualObj = mapper.readTree(parser);
                JsonNode data = actualObj.get("data");
                if (data != null) {
                    JsonNode responsesUserId = data.get("user_id");
                    if (responsesUserId != null) {
                        return responsesUserId.asText().equals(userId);
                    }
                }
                return false;
            }
        } catch (Exception ignored) {
        }
        return false;
    }
}