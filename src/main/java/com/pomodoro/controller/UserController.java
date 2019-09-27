package com.pomodoro.controller;

import com.pomodoro.model.User;
import com.pomodoro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
public class UserController {
    private final
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/users/logIn", method = RequestMethod.POST)
    @ResponseBody
    public User logIn(@RequestBody Map<String, Object> body) throws ResponseStatusException {
        User user = userService.logIn(body.get("username").toString(), body.get("password").toString());
        return user;
    }

}
