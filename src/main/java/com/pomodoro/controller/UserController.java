package com.pomodoro.controller;

import com.pomodoro.model.dto.*;
import com.pomodoro.model.group.Group;
import com.pomodoro.model.group.GroupInvitation;
import com.pomodoro.model.o2auth.FacebookUser;
import com.pomodoro.model.request.UpdateUserDetails;
import com.pomodoro.model.todo.GroupToDo;
import com.pomodoro.model.todo.UserToDo;
import com.pomodoro.model.user.Pomodoro;
import com.pomodoro.model.user.Settings;
import com.pomodoro.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        UserDetails userDetails = userService
                .loadUserDetailsByUsername(authenticationRequest.getUsername());

        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @RequestMapping(value = "/facebookLogin", method = RequestMethod.POST)
    public ResponseEntity<?> facebookLogin(@Valid @RequestBody FacebookUser facebookUser) {
        Map<String, String> responseEntity = new HashMap<>();
        if (userService.facebookAccessTokenValid(facebookUser.getAuthToken(), facebookUser.getId())) {
            User user = userRepository.findUserByEmail(facebookUser.getEmail());
            if (user == null) {
                log.debug("User with email {} was not found registering as new user", facebookUser.getEmail());
                RegisterUser newUser = new RegisterUser();
                newUser.setUsername(facebookUser.getName());
                newUser.setFirstName(facebookUser.getFirstName());
                newUser.setLastName(facebookUser.getLastName());
                newUser.setEmail(facebookUser.getEmail());
                userService.registerNewUser(newUser);
            }
            String token = jwtTokenUtil.generateToken(user);
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            responseEntity.put("error", "Invalid access token");
        }
        return ResponseEntity.ok().body(responseEntity);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody RegisterUser newUser) throws NoSuchFieldException {
        Map<String, String> responseEntity = new HashMap<>();
        int status = 400;
        if (userRepository.findUserByUsername(newUser.getUsername()) != null) {
            responseEntity.put("username", String.format("user with name %s already exists", newUser.getUsername()));
        }
        if (userRepository.findUserByEmail(newUser.getEmail()) != null) {
            responseEntity.put("email", String.format("user with email %s already exists", newUser.getEmail()));
        }
        if (responseEntity.size() == 0) {
            userService.registerNewUser(newUser);
            status = 200;
            responseEntity.put("success", "You were successfully registered!");
        }
        return ResponseEntity.status(status).body(responseEntity);
    }

    @RequestMapping(value = "/userDetails", method = RequestMethod.POST)
    public User getUserDetails(HttpServletRequest req) {
        return userService.getUserFromToken(userService.getTokenFromRequest(req));
    }

    @RequestMapping(value = "/updateDetails", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(HttpServletRequest req, @RequestBody @Valid UpdateUserDetails updatedUser) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Map<String, String> responseEntity = new HashMap<>();
        int status = 400;
        if (userRepository.findUserByUsername(updatedUser.getUsername()) != null && !userRepository.findUserByUsername(updatedUser.getUsername()).getEmail().equals(user.getEmail())) {
            responseEntity.put("username", String.format("user with name %s already exists", updatedUser.getUsername()));
        }
        if (userRepository.findUserByEmail(updatedUser.getEmail()) != null && !userRepository.findUserByEmail(updatedUser.getEmail()).getUsername().equals(user.getUsername())) {
            responseEntity.put("email", String.format("user with email %s already exists", updatedUser.getEmail()));
        }
        String firstName = updatedUser.getFirstName();
        String lastName = updatedUser.getLastName();
        if (
                (firstName != null && firstName.equals(updatedUser.getFirstName()))
                        && (lastName != null && lastName.equals(updatedUser.getLastName()))
                        && user.getUsername().equals(updatedUser.getUsername())
                        && user.getEmail().equals(updatedUser.getEmail())
        ) {
            responseEntity.put("error", "No value was updated");
        }
        if (responseEntity.size() == 0) {
            try {
                userService.updateUser(user, updatedUser);
                User newUser = userRepository.findUserByUsername(updatedUser.getUsername());
                status = 200;
                String token = jwtTokenUtil.generateToken(newUser);
                responseEntity.put("success", "Personal information were successfully updated");
                responseEntity.put("newToken", token);
            } catch (Exception e) {
                log.error("Error while authenticating user {} {}", updatedUser.getUsername(), e);
                responseEntity.put("error", "Unexpected error has occured");
            }
        }
        return ResponseEntity.status(status).body(responseEntity);

    }

    @RequestMapping(value = "/updateSettings", method = RequestMethod.POST)
    public void updateUserSettings(HttpServletRequest req, @RequestBody Settings updatedSettings) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        userService.updateUserSettings(user, updatedSettings);
    }

    @RequestMapping(value = "/userPomodoros", method = RequestMethod.POST)
    public List<Pomodoro> updateUser(HttpServletRequest req) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return user.getPomodoros();
    }

    @RequestMapping(value = "/userTodos", method = RequestMethod.POST)
    public List<UserToDo> getUserTodos(HttpServletRequest req) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return user.getUserToDos();
    }

    @RequestMapping(value = "/groupTodos", method = RequestMethod.POST)
    public Set<GroupToDo> getGroupTodos(HttpServletRequest req) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return user.getGroupToDos();
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(HttpServletRequest req, @Valid @RequestBody ChangePassword
            changePassword) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Map<String, String> responseEntity = new HashMap<>();
        int status = 400;
        if (changePassword.getOldPassword().equals(changePassword.getNewPassword())) {
            responseEntity.put("newPasswordConfirm", "Passwords match");
        }
        if (!userService.passwordBelongsToTheUser(user, changePassword.getOldPassword())) {
            responseEntity.put("oldPassword", "Incorrect password");
        }
        if (responseEntity.size() == 0) {
            status = 200;
            responseEntity.put("success", "Password was successfully changed");
            userService.changePassword(user, changePassword.getOldPassword(), changePassword.getNewPassword());
        }
        return ResponseEntity.status(status).body(responseEntity);
    }

    @RequestMapping(value = "/not-accepted-group-invitations", method = RequestMethod.POST)
    public ResponseEntity<?> getGroupInitations(HttpServletRequest req) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        List<GroupInvitation>invitations=user.getGroupInvitations();
        return ResponseEntity.ok(user.getGroupInvitations().stream().filter(groupInvitation -> !groupInvitation.getAccepted()));
    }

    @RequestMapping(value = "/accept-invitation", method = RequestMethod.POST)
    public ResponseEntity<?> acceptGroupInvitation(HttpServletRequest req, @Valid @RequestBody GroupInvitation groupInvitation) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Map<String, String> responseEntity = new HashMap<>();
        int status = 200;
        Group group = groupRepository.findPomodoroGroupByName(groupInvitation.getGroup().getName()).get(0);
        userService.addUserToGroup(group, user);
        groupInvitation = groupInvitationRepository.findGroupInvitationById(groupInvitation.getId());
        groupInvitation.setAccepted(true);
        groupInvitationRepository.save(groupInvitation);
        responseEntity.put("success", "invitation accepted");
        return ResponseEntity.status(status).body(responseEntity);
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping(value = "/users-on-hint", method = RequestMethod.POST)
    public List<User> getGroupInitations(HttpServletRequest req, @RequestBody Hint hint) {
        List<User> users = userRepository.findUserByUsernameStartingWith(hint.getValue());
        return users;
    }

    @RequestMapping(value = "/remove-toDoId", method = RequestMethod.POST)
    public ResponseEntity<?> deleteToDoFromTheGroup(HttpServletRequest req, @RequestBody List<Integer> todoIds) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Map<String, String> responseEntity = new HashMap<>();
        responseEntity.put("success", "Successfully removed");
        userTodoRepository.deleteUserTodos(todoIds);
        return ResponseEntity.ok().body(responseEntity);
    }
}

