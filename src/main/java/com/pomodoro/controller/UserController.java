package com.pomodoro.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.config.WebSecurityConfig;
import com.pomodoro.model.*;
import com.pomodoro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController extends AbstractController{

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
        userService.updateUserSettings(user,updatedSettings);
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


}

