package com.pomodoro.service.serviceimplementation;

import com.pomodoro.model.dto.RegisterUser;
import com.pomodoro.model.user.Settings;
import com.pomodoro.model.request.UpdateUserDetails;
import com.pomodoro.model.user.User;
import com.pomodoro.service.IUserService;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

 class LimitedUserService implements IUserService {

    private final IUserService userService;

    public LimitedUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return null;
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest req) {
        return null;
    }

    @Override
    public User getUserFromToken(String jwtToken) {
        return null;
    }

    @Override
    public void updateUser(User currentUser, UpdateUserDetails updatedUser) {

    }

    @Override
    public void updateUserSettings(User currentUser, Settings updatedSettings) {

    }

    @Override
    public boolean passwordBelongsToTheUser(User user, String password) {
        return false;
    }

    @Override
    public void registerNewUser(RegisterUser newUser) {

    }

    @Override
    public boolean facebookAccessTokenValid(String inputToken, String userId) {
        return false;
    }
}
