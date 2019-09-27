package com.pomodoro.service;

import com.pomodoro.model.User;
import com.pomodoro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User logIn(String email, String password) throws ResponseStatusException {
        User foundUser = userRepository.findUserByEmail(email);
        if (foundUser == null || !encoder.matches(password, foundUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = new User();
        user.setFirstName(foundUser.getFirstName());
        user.setLastName(foundUser.getLastName());
        user.setEmail(foundUser.getEmail());
        user.setToken("token");
        return user;
    }
}
