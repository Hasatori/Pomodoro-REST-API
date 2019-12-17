package com.pomodoro.controller;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.model.*;
import com.pomodoro.service.UserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController extends AbstractController {

    UserController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        super(authenticationManager, jwtTokenUtil, userService);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody User newUser) throws NoSuchFieldException {
        Map<String, String> responseEntity = new HashMap<>();
        int status = 400;
        if (!userService.usernameUnique(newUser.getUsername())) {
            responseEntity.put("username", String.format("user with name %s already exists", newUser.getUsername()));
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

