package com.pomodoro.service;

import com.pomodoro.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        User user = authenticationService.authenticate("hradil.o@email.cz","$2y$12$voZgQwPT/8x3DmSSOojg2OosFYSfEgTptDNZnICZS9jbZa7AJ.c3y");

        Assert.assertEquals(user.getPassword(),"$2y$12$voZgQwPT/8x3DmSSOojg2OosFYSfEgTptDNZnICZS9jbZa7AJ.c3y");
        Assert.assertNull(user.getToken());
        Assert.assertEquals(user.getFirstName(),"Oldřich");
        Assert.assertEquals(user.getLastName(),"Hradil");
    }
}