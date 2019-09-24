package com.pomodoro.controller;

import com.pomodoro.model.User;
import com.pomodoro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthenticationController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/users/authenticate", method = RequestMethod.GET)
    @ResponseBody
    public User authenticate(@RequestParam("email") String email, @RequestParam("password") String password) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        return user;
    }

}
