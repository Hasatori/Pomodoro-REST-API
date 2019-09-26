package com.pomodoro.service;

import com.pomodoro.model.User;
import com.pomodoro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String email, String password) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        return user;
    }
}
