package com.pomodoro.service.serviceimplementation;

import com.pomodoro.service.IGroupService;
import com.pomodoro.service.IPomodoroService;
import com.pomodoro.service.IStorageService;
import com.pomodoro.service.IUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceProvider {

    private final IUserService userService;
    private final IGroupService groupService;
    private final IPomodoroService pomodoroService;
    private final IStorageService storageService;

    public ServiceProvider(@Qualifier("basicUserService") IUserService userService, @Qualifier("basicGroupService") IGroupService groupService, @Qualifier("basicPomodoroService") IPomodoroService pomodoroService, @Qualifier("basicStorageService") IStorageService storageService) {
        this.userService = userService;
        this.groupService = groupService;
        this.pomodoroService = pomodoroService;
        this.storageService = storageService;
    }

    @Bean(name = "limitedUserService")
    public IUserService limitedUserService() {
        return new LimitedUserService(userService);
    }

    @Bean(name = "limitedGroupService")
    public IGroupService limitedGroupService() {
        return new LimitedGroupService(groupService, storageService);
    }

    @Bean(name = "limitedPomodoroService")
    public IPomodoroService limitedPomodoroService() {
        return new LimitedPomodoroService(pomodoroService);
    }

}
