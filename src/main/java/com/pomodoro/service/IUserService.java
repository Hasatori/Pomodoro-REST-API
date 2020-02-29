package com.pomodoro.service;

import com.pomodoro.model.*;
import com.pomodoro.model.request.UpdateUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface IUserService {


    UserDetails loadUserByUsername(String username);

    String getTokenFromRequest(HttpServletRequest req);

    User getUserFromToken(String jwtToken);

    void updateUser(User currentUser, UpdateUserDetails updatedUser);

    void updateUserSettings(User currentUser, Settings updatedSettings);

    boolean passwordBelongsToTheUser(User user, String password);


    void registerNewUser(RegisterUser newUser);


    boolean facebookAccessTokenValid(String inputToken, String userId) ;
}
