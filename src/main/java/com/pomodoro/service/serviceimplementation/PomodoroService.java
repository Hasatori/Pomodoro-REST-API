package com.pomodoro.service.serviceimplementation;

import com.pomodoro.model.user.Pomodoro;
import com.pomodoro.model.user.User;
import com.pomodoro.service.IPomodoroService;
import org.springframework.stereotype.Service;

@Service("basicPomodoroService")
 class PomodoroService implements IPomodoroService {

    @Override
    public Pomodoro createPomodoroAndReturn(User user) {
        return null;
    }

    @Override
    public Pomodoro getLastPomodoro(User user) {
        return null;
    }

    @Override
    public void stopPomodoro(User user, Pomodoro pomodoro) {

    }

    @Override
    public Pomodoro getLastPomodoroForUser(String username) {
        return null;
    }
}
