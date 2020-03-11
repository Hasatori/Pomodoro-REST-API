package com.pomodoro.service;

import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface IPomodoroService {


    Pomodoro createPomodoroAndReturn(User user);

    Pomodoro getLastPomodoro(User user);

    void stopPomodoro(User user, Pomodoro pomodoro);

    Pomodoro getLastPomodoroForUser(String username);
}
