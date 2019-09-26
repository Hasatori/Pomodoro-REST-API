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
    @Autowired
    AuthenticationService authenticationService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
    @ResponseBody
    public User authenticate(@RequestBody Map<String, Object> body) {
        User user = authenticationService.authenticate(body.get("email").toString(), body.get("password").toString());
        return user;
    }

}
