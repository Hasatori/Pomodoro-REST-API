package com.pomodoro.service.serviceimplementation;

import com.pomodoro.model.Group;
import com.pomodoro.model.Pomodoro;
import com.pomodoro.model.User;
import com.pomodoro.service.IPomodoroService;

import java.util.Set;

class LimitedPomodoroService implements IPomodoroService {

    private static final Integer POMODORO_LIMIT=100000;

    private final IPomodoroService pomodoroService;


    public LimitedPomodoroService(IPomodoroService pomodoroService) {
        this.pomodoroService = pomodoroService;
    }

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
