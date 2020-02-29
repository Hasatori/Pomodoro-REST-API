package com.pomodoro.service;

import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import org.springframework.stereotype.Service;

@Service
public interface IPomodoroService {


    Pomodoro createPomodoroAndReturn(User user);

    Pomodoro getLastPomodoro(User user);

    void stopPomodoro(User user, Pomodoro pomodoro);

    Pomodoro getLastPomodoroForUser(String username);
}
