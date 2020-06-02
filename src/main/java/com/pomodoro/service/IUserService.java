package com.pomodoro.service;

import com.pomodoro.model.dto.RegisterUser;
import com.pomodoro.model.message.DirectMessage;
import com.pomodoro.model.request.UpdateUserDetails;
import com.pomodoro.model.user.Settings;
import com.pomodoro.model.user.User;
import com.pomodoro.utils.RequestDataNotValidException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public interface IUserService {


    UserDetails loadUserDetailsByUsername(String username);

    User loadUserByUsername(String username);

    String getTokenFromRequest(HttpServletRequest req);

    User getUserFromToken(String jwtToken);

    void updateUser(User currentUser, UpdateUserDetails updatedUser);

    void updateUserSettings(User currentUser, Settings updatedSettings);

    boolean passwordBelongsToTheUser(User user, String password);


    void registerNewUser(RegisterUser newUser);

    boolean facebookAccessTokenValid(String inputToken, String userId) ;

    DirectMessage createDirectMessage(User author, User recipient, String value) throws RequestDataNotValidException;

    DirectMessage createDirectMessageAttachment(User author, User recipient, MultipartFile file) throws RequestDataNotValidException;

    DirectMessage createAnswerForMessage(User author, User recipient, String value, DirectMessage answeredMessage) throws RequestDataNotValidException;

}
