package com.pomodoro.controller;

import com.pomodoro.config.JwtTokenUtil;
import com.pomodoro.model.User;
import com.pomodoro.repository.UserRepository;
import com.pomodoro.service.UserService;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;

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

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void registerNewUser_usernameAndEmailAndPasswordAreBlank_ShouldReturnErrorMessages() throws Exception {
        String user = "{\"username\": \"\", \"email\" : \"\", \"password\" : \"\"}";
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

    @Test
    public void facebookLogin_validAuthToken_ShouldReturnJwtAccessToken() throws Exception {
        User user = new User();
        user.setEmail("test");
        when(userService.facebookAccessTokenValid("test")).thenReturn(true);
        when(userRepository.findUserByEmail("test")).thenReturn(user);
        when(jwtTokenUtil.generateToken(user)).thenReturn("test");

        String stringUser = "{\"id\" :123" +
                ", \"authToken\":\"test\"" +
                ", \"email\":\"test\"" +
                ", \"name\":\"test\"}";
        mvc.perform(MockMvcRequestBuilders.post("/facebookLogin")
                .content(stringUser)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", Is.is("test")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

}



