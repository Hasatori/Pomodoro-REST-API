package com.pomodoro.service;

import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;


    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        User user = userService.logIn("hradil.o@email.cz", "$2y$12$voZgQwPT/8x3DmSSOojg2OosFYSfEgTptDNZnICZS9jbZa7AJ.c3y");
        System.out.println(user);

        Set<Pomodoro> pomodoros =user.getPomodoros();

                Assert.assertEquals(user.getPassword(), "$2y$12$voZgQwPT/8x3DmSSOojg2OosFYSfEgTptDNZnICZS9jbZa7AJ.c3y");
        Assert.assertNull(user.getToken());
        Assert.assertEquals(user.getFirstName(), "Oldřich");
        Assert.assertEquals(user.getLastName(), "Hradil");

        Assert.assertEquals(1,pomodoros.size());
    }
}
