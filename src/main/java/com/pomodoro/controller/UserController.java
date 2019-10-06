package com.pomodoro.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.config.WebSecurityConfig;
import com.pomodoro.model.JwtRequest;
import com.pomodoro.model.JwtResponse;
import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import com.pomodoro.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;


    public UserController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
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
