package com.pomodoro.controller;

import com.google.gson.Gson;
import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.model.request.UpdateUserDetails;
import com.pomodoro.model.user.User;
import com.pomodoro.model.o2auth.FacebookUser;
import com.pomodoro.service.repository.UserRepository;
import com.pomodoro.service.serviceimplementation.UserService;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {


    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    WebApplicationContext webApplicationContext;

    private Gson gson;
    private FacebookUser testFacebookUser;
    private User testUser;

    @Before
    public void setUp() {
        this.gson = new Gson();
        this.testFacebookUser = new FacebookUser();
        this.testUser = new User();
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void registerNewUser_usernameAndEmailAndPasswordAreBlank_ShouldReturnErrorMessages() throws Exception {
        String user = "{\"username\": \"\", \"email\" : \"\", \"password\" : \"d\"}";
        mvc.perform(MockMvcRequestBuilders.post("/register")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is("Password must be between 8 to 40 characters long, must contain at least one digit, one upper case character and one lower case character")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is("Name is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("Invalid email format")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void registerNewUser_passwordInIncorrectFormat_ShouldReturnErrorMessages() throws Exception {
        String user = "{\"password\" : \"test\"}";
        mvc.perform(MockMvcRequestBuilders.post("/register")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is("Password must be between 8 to 40 characters long, must contain at least one digit, one upper case character and one lower case character")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void facebookLogin_allValuesAreBlank_ShouldReturnErrorMessages() throws Exception {
        String user = "{\"password\" : \"test\"}";
        mvc.perform(MockMvcRequestBuilders.post("/facebookLogin")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authToken", Is.is("Authentication token is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is("Id is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("Email is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

 /*   @Test
    public void facebookLogin_validAuthToken_ShouldReturnJwtAccessToken() throws Exception {
        String jwtToken = "test";
        testFacebookUser.setEmail("test");
        testFacebookUser.setId("123");
        testFacebookUser.setName("test");
        testFacebookUser.setAuthToken("test");
        testUser.setEmail(testFacebookUser.getEmail());

        when(userService.facebookAccessTokenValid(testFacebookUser.getAuthToken(), testFacebookUser.getId())).thenReturn(true);
        when(userRepository.findUserByEmail(testFacebookUser.getEmail())).thenReturn(testUser);
        when(jwtTokenUtil.generateToken(testUser)).thenReturn(jwtToken);
        String userJson = gson.toJson(testFacebookUser);
        mvc.perform(MockMvcRequestBuilders.post("/facebookLogin")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", Is.is(jwtToken)))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }*/

    @Test
    public void updateUser_allValuesAreBlank_ShouldReturnErrorMessages() throws Exception {
        UpdateUserDetails updateUserDetails = new UpdateUserDetails();
        String userJson = gson.toJson(updateUserDetails);
        mvc.perform(MockMvcRequestBuilders.post("/updateDetails")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is("Name is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("Email is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void updateUser_onlyUsernameIsMissing_ShouldReturnErrorMessages() throws Exception {
        UpdateUserDetails updateUserDetails = new UpdateUserDetails();
        updateUserDetails.setEmail("test@email.cz");
        String userJson = gson.toJson(updateUserDetails);
        mvc.perform(MockMvcRequestBuilders.post("/updateDetails")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is("Name is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").doesNotExist())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void updateUser_onlyEmailIsMissing_ShouldReturnErrorMessages() throws Exception {
        UpdateUserDetails updateUserDetails = new UpdateUserDetails();
        updateUserDetails.setUsername("test");
        String userJson = gson.toJson(updateUserDetails);
        mvc.perform(MockMvcRequestBuilders.post("/updateDetails")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("Email is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").doesNotExist())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void updateUser_userWithSameUsernameAlreadyExists_ShouldReturnUsernameError() throws Exception {
        String jwtToken = "test";
        String userName = "test";
        String email="test@email.cz";
        testUser.setEmail(email);
        User duplicateNameUser = new User();
        duplicateNameUser.setUsername(userName);
        duplicateNameUser.setEmail("test2@email.cz");
        when(userService.getTokenFromRequest(any())).thenReturn(jwtToken);
        when(userService.getUserFromToken(jwtToken)).thenReturn(testUser);
        when(userRepository.findUserByUsername(userName)).thenReturn(duplicateNameUser);
        UpdateUserDetails updateUserDetails = new UpdateUserDetails();
        updateUserDetails.setUsername(userName);
        updateUserDetails.setEmail(email);
        String userJson = gson.toJson(updateUserDetails);
        mvc.perform(MockMvcRequestBuilders.post("/updateDetails")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is(String.format("user with name %s already exists", userName))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").doesNotExist())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void updateUser_userWithSameEmailAlreadyExists_ShouldReturnEmailError() throws Exception {
        String jwtToken = "test";
        String email = "test@email.cz";
        User duplicateEmailUser = new User();
        duplicateEmailUser.setUsername("test2");
        duplicateEmailUser.setEmail(email);
        when(userService.getTokenFromRequest(any())).thenReturn(jwtToken);
        when(userService.getUserFromToken(jwtToken)).thenReturn(testUser);
        when(userRepository.findUserByEmail(email)).thenReturn(duplicateEmailUser);
        UpdateUserDetails updateUserDetails = new UpdateUserDetails();
        updateUserDetails.setUsername("test");
        updateUserDetails.setEmail(email);
        String userJson = gson.toJson(updateUserDetails);
        mvc.perform(MockMvcRequestBuilders.post("/updateDetails")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is(String.format("user with email %s already exists", email))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").doesNotExist())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void updateUser_noValueWasUpdated_ShouldReturnErrorMessage() throws Exception {
        String jwtToken = "test";
        String firstName = "test";
        String lastName = "test";
        String username = "test";
        String email = "test@email.cz";
        testUser.setFirstName(firstName);
        testUser.setLastName(lastName);
        testUser.setEmail(email);
        testUser.setUsername(username);

        UpdateUserDetails updateUserDetails = new UpdateUserDetails();
        updateUserDetails.setUsername(username);
        updateUserDetails.setFirstName(firstName);
        updateUserDetails.setLastName(lastName);
        updateUserDetails.setEmail(email);

        when(userService.getTokenFromRequest(any())).thenReturn(jwtToken);
        when(userService.getUserFromToken(jwtToken)).thenReturn(testUser);
        when(userRepository.findUserByEmail(updateUserDetails.getEmail())).thenReturn(testUser);
        when(userRepository.findUserByUsername(updateUserDetails.getUsername())).thenReturn(testUser);

        String userJson = gson.toJson(updateUserDetails);
        mvc.perform(MockMvcRequestBuilders.post("/updateDetails")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Is.is("No value was updated")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}



