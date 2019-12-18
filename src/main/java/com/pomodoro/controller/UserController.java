package com.pomodoro.controller;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.model.*;
import com.pomodoro.model.o2auth.FacebookUser;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    UserController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, UserRepository userRepository) {
        super(authenticationManager, jwtTokenUtil, userService, userRepository);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());

        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @RequestMapping(value = "/facebookLogin", method = RequestMethod.POST)
    public ResponseEntity<?> facebookLogin(@Valid @RequestBody FacebookUser facebookUser) {
        Map<String, String> responseEntity = new HashMap<>();
        try {
            if (userService.facebookAccessTokenValid(facebookUser.getAuthToken(),facebookUser.getId())) {
                User user = userRepository.findUserByEmail(facebookUser.getEmail());
                if (user == null) {
                    log.debug("User with email {} was not found registering as new user",facebookUser.getEmail());
                    user = new User();
                    user.setUsername(facebookUser.getName());
                    user.setFirstName(facebookUser.getFirstName());
                    user.setLastName(facebookUser.getLastName());
                    user.setEmail(facebookUser.getEmail());
                    userService.registerNewUser(user);
                }
                String token = jwtTokenUtil.generateToken(user);
                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                responseEntity.put("Error", "Invalid access token");
            }
        } catch (IOException e) {
            log.error("Error while validating facebook access token {}", facebookUser.getAuthToken());
            responseEntity.put("Error", "Error while validating access token ");
        }
            return ResponseEntity.ok().body(responseEntity);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody User newUser) throws NoSuchFieldException {
        Map<String, String> responseEntity = new HashMap<>();
        int status = 400;
        if (userRepository.findUserByUsername(newUser.getUsername()) != null) {
            responseEntity.put("username", String.format("user with name %s already exists", newUser.getUsername()));
        } else if (userRepository.findUserByEmail(newUser.getEmail()) != null) {
            responseEntity.put("email", String.format("user with email %s already exists", newUser.getEmail()));
        } else {
            userService.registerNewUser(newUser);
            status = 200;
            responseEntity.put("Success", "You were successfully registered!");
        }
        return ResponseEntity.status(status).body(responseEntity);
    }

    @RequestMapping(value = "/userDetails", method = RequestMethod.POST)
    public User getUserDetails(HttpServletRequest req) {
        return userService.getUserFromToken(userService.getTokenFromRequest(req));
    }

    @RequestMapping(value = "/updateDetails", method = RequestMethod.POST)
    public void updateUser(HttpServletRequest req, @RequestBody User updatedUser) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        userService.updateUser(user, updatedUser);
    }

    @RequestMapping(value = "/updateSettings", method = RequestMethod.POST)
    public void updateUserSettings(HttpServletRequest req, @RequestBody Settings updatedSettings) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        userService.updateUserSettings(user, updatedSettings);
    }

    @RequestMapping(value = "/userPomodoros", method = RequestMethod.POST)
    public List<Pomodoro> updateUser(HttpServletRequest req) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        return user.getPomodoros();
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public void changePassword(HttpServletRequest req, @RequestBody Map<String, String> body) {
        User user = userService.getUserFromToken(userService.getTokenFromRequest(req));
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        userService.changePassword(user, oldPassword, newPassword);
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}

