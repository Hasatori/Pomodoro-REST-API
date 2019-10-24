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
public class GroupController extends AbstractController{

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
        userService.createGroup(user,group.getName(), group.isPublic());
    }
}
