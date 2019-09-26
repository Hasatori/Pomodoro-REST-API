package com.pomodoro.controller;

import com.pomodoro.model.User;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class AuthenticationController {
    final
    AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
    @ResponseBody
    public User authenticate(@RequestBody Map<String, Object> body) {
        User user = authenticationService.authenticate(body.get("username").toString(), body.get("password").toString());
        return user;
    }

}
