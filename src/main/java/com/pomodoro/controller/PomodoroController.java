package com.pomodoro.controller;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import com.pomodoro.repository.GroupRepository;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PomodoroController extends AbstractController{

    PomodoroController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, UserRepository userRepository, GroupRepository groupRepository) {
        super(authenticationManager, jwtTokenUtil, userService,userRepository, groupRepository);
    }

    @RequestMapping(value = "/pomodoro/update", method = RequestMethod.POST)
    public Pomodoro getLastPomodoro(HttpServletRequest req) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return userService.getLastPomodoro(user);
    }
    @RequestMapping(value = "/groups/update/{userName}", method = RequestMethod.POST)
    public Pomodoro getLastPomodoroForUser(HttpServletRequest req, @PathVariable("userName") String userName) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return userService.getLastPomodoroForUser(userName);
    }

}
