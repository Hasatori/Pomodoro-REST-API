package com.pomodoro.controller;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.model.Group;
import com.pomodoro.model.User;
import com.pomodoro.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GroupController extends AbstractController {

    GroupController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        super(authenticationManager, jwtTokenUtil, userService);
    }

    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    public Set<Group> getGroups(HttpServletRequest req) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return user.getGroups();
    }

    @RequestMapping(value = "/groups/{groupName}", method = RequestMethod.POST)
    public Set<User> getUsersForGroup(HttpServletRequest req, @PathVariable(required = true) String groupName) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return userService.getUsersForGroup(groupName, user);
    }


    @RequestMapping(value = "/group/create", method = RequestMethod.POST)
    public void createGroup(HttpServletRequest req, @RequestBody Group group) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        userService.createGroup(user, group.getName(), group.isPublic());
    }

    @RequestMapping(value = "/groups/{groupName}/add/{username}", method = RequestMethod.POST)
    public void addUserToTheGroup(HttpServletRequest req, @PathVariable("groupName") String groupName, @PathVariable("username") String username) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        userService.addUserToGroup(user, groupName, username);
    }
    @RequestMapping(value = "/groups/{groupName}/delete/{username}", method = RequestMethod.POST)
    public void deleteUserFromTheGroup(HttpServletRequest req, @PathVariable("groupName") String groupName, @PathVariable("username") String username) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        userService.addUserToGroup(user, groupName, username);
    }
}
