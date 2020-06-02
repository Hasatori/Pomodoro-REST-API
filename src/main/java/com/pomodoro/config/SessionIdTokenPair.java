package com.pomodoro.config;

import lombok.Getter;

@Getter
public class SessionIdTokenPair {

    private final String sessionId,token;

    public SessionIdTokenPair(String sessionId, String token){
        this.sessionId = sessionId;
        this.token=token;
    }

}
