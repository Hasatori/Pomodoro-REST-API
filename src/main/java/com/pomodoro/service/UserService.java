package com.pomodoro.service;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.model.Group;
import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import com.pomodoro.repository.PomodoroRepository;
import com.pomodoro.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PomodoroRepository pomodoroRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtTokenUtil jwtTokenUtil;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PomodoroRepository pomodoroRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.pomodoroRepository = pomodoroRepository;
        this.jwtTokenUtil = jwtTokenUtil;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findUserByUsername(username);
        if (foundUser != null) {
            return foundUser;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public String getTokenFromRequest(HttpServletRequest req) {
        String jwtToken = null;
        final String requestTokenHeader = req.getHeader("Authorization");
        final String socketHeader = req.getHeader("Sec-WebSocket-Key");
        final String socketToken = req.getParameter("token");
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
        } else if (socketHeader != null && socketToken != null) {
            jwtToken = socketToken.substring(7);
        }
        return jwtToken;
    }

    public User getUserFromToken(String jwtToken) {
        String username = null;
        try {
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Token has expired");
        }
        return userRepository.findUserByUsername(username);
    }

    public void updateUser(User currentUser, User updatedUser) {
        userRepository.updateUserDetails(currentUser.getId(), updatedUser.getUsername(), updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getEmail());
    }

    public void changePassword(User user, String oldPassword, String newPassoword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPassword, user.getPassword())) {
            userRepository.updatePassword(user.getId(), encoder.encode(newPassoword));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    public Pomodoro createPomodoroAndReturn(User user) {
        pomodoroRepository.insertNewPomodoro(user.getId(), LocalDateTime.now(), 10, 300, false);
        Pomodoro pomodoro = Collections.max(user.getPomodoros(), Comparator.comparing(Pomodoro::getCreationTimestamp));
        return pomodoro;
    }

    public Pomodoro getLastPomodoro(User user) {
        Pomodoro pomodoro = Collections.max(user.getPomodoros(), Comparator.comparing(Pomodoro::getCreationTimestamp));
        return pomodoro;
    }

    public void stopPomodoro(User user, Pomodoro pomodoro) {
        pomodoroRepository.stopPomodoro(user.getId(), pomodoro.getCreationTimestamp());
    }

}
