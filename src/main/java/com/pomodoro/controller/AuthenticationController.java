package com.pomodoro.controller;

import com.pomodoro.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthenticationController {
    @RequestMapping(value = "/users/authenticate", method = RequestMethod.GET)
    @ResponseBody
    public User authenticate() {
        return new User("Oldřich", "Hradil", "hradil.o@email.cz", "testingToken");
    }

}
