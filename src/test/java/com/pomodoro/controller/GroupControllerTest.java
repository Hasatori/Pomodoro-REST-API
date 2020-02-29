package com.pomodoro.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pomodoro.model.Group;
import com.pomodoro.model.User;
import com.pomodoro.model.o2auth.FacebookUser;
import com.pomodoro.model.request.GroupCreationRequest;
import com.pomodoro.service.IGroupService;
import com.pomodoro.service.serviceimplementation.UserService;
import com.pomodoro.utils.RequestDataNotValidException;
import com.pomodoro.utils.RequestError;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class GroupControllerTest {


    private MockMvc mvc;

    @MockBean
    @Qualifier("limitedGroupService")
    private IGroupService groupService;

    @MockBean
    private UserService userService;

    @Autowired
    WebApplicationContext webApplicationContext;

    private Gson gson;
    private FacebookUser testFacebookUser;
    private User testUser;

    @Before
    public void setUp() {
        this.gson = new GsonBuilder()
                .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        this.testFacebookUser = new FacebookUser();
        this.testUser = new User();
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String jwtToken = "test";
        when(userService.getTokenFromRequest(any())).thenReturn(jwtToken);
        when(userService.getUserFromToken(jwtToken)).thenReturn(testUser);
    }

    @Test
    public void createGroup__ShouldReturnErrorMessages() throws Exception {
        String groupName = "test";
        String layoutImage = "test";
        Date created = new Date();
        Boolean isPublic = false;
        String fieldName1 = "groupName";
        String expectedMessage1 = "Group with this name already exists";
        String fieldName2 = "groupLimit";
        String expectedMessage2 = "Group count limit exceeded";

        List<RequestError> errorList = new ArrayList<>();
        errorList.add(new RequestError(fieldName1, expectedMessage1));
        errorList.add(new RequestError(fieldName2,expectedMessage2));
        doThrow(new RequestDataNotValidException(errorList)).when(groupService).createGroup(testUser, layoutImage, groupName, isPublic,null);

       GroupCreationRequest groupCreationRequest = new GroupCreationRequest();
        groupCreationRequest.setLayoutImage(layoutImage);
        groupCreationRequest.setGroupName(groupName);
        groupCreationRequest.setIsPublic(isPublic);
        groupCreationRequest.setDescription(null);
        String groupJson = gson.toJson(groupCreationRequest);
        mvc.perform(MockMvcRequestBuilders.post("/group/create")
                .content(groupJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(String.format("$.%s", fieldName1), Is.is(expectedMessage1)))
                .andExpect(MockMvcResultMatchers.jsonPath(String.format("$.%s", fieldName2), Is.is(expectedMessage2)))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8));
    }


}



