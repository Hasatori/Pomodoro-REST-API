package com.pomodoro.service.serviceimplementation;

import com.pomodoro.model.dto.RegisterUser;
import com.pomodoro.model.message.DirectMessage;
import com.pomodoro.model.user.Settings;
import com.pomodoro.model.request.UpdateUserDetails;
import com.pomodoro.model.user.User;
import com.pomodoro.service.IUserService;
import com.pomodoro.utils.RequestDataNotValidException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

 class LimitedUserService implements IUserService {

    private final IUserService userService;

    public LimitedUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserDetailsByUsername(String username) {
        return null;
    }

     @Override
     public User loadUserByUsername(String username) {
         return this.userService.loadUserByUsername(username);
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

     @Override
     public DirectMessage createDirectMessage(User author, User recipient, String value) throws RequestDataNotValidException {
         return null;
     }

     @Override
     public DirectMessage createDirectMessageAttachment(User author, User recipient, MultipartFile file) throws RequestDataNotValidException {
         return null;
     }
 }
