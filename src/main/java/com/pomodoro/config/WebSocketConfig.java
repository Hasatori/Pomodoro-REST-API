package com.pomodoro.config;

import com.pomodoro.model.user.User;
import com.pomodoro.service.serviceimplementation.UserService;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final UserService userService;
    private final JwtTokenUtil tokenUtil;
    public static final HashMap<String, List<SessionIdTokenPair>> SESSIONID_USER = new HashMap<>();


    public WebSocketConfig(UserService userService, JwtTokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/pomodoro", "/group", "/user");
        config.setUserDestinationPrefix("/user");


    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                String messageType = ((SimpMessageType) message.getHeaders().get("simpMessageType")).name();
                boolean authorized = false;
                if ("CONNECT".equals(messageType)) {
                    String token = getTokenFromMessage(message);
                    if (token != null) {
                        User user = userService.getUserFromToken(token);
                        String sessionId = getSessionIdFromMessage(message);
                        // if token is valid configure Spring Security to manually set authentication
                        if (tokenUtil.validateToken(token, user) && accessor != null) {
                            authorized = true;
                            accessor.setUser(user);
                            SessionIdTokenPair sessionIdTokenPair = new SessionIdTokenPair(sessionId, token);
                            List<SessionIdTokenPair> sessionIdTokenPairs = Optional.ofNullable(SESSIONID_USER.get(user.getUsername())).orElse(new ArrayList<>());
                            sessionIdTokenPairs.add(sessionIdTokenPair);
                            SESSIONID_USER.put(user.getUsername(), sessionIdTokenPairs);
                        }
                    }
                }
                if ("DISCONNECT".equals(messageType)) {
                    String sessionId = getSessionIdFromMessage(message);
                    for (Map.Entry<String, List<SessionIdTokenPair>> pair : SESSIONID_USER.entrySet()) {
                        Optional<SessionIdTokenPair> optionalSessionIdTokenPair = pair
                                .getValue()
                                .stream()
                                .filter(sessionIdTokenPair -> sessionIdTokenPair.getSessionId().equals(sessionId))
                                .findFirst();
                        if (optionalSessionIdTokenPair.isPresent()) {
                            pair.getValue().remove(optionalSessionIdTokenPair.get());
                            break;
                        }
                    }
                    SESSIONID_USER.forEach((username, sessionIdTokenPair) -> {

                    });

                    SESSIONID_USER.remove(sessionId);
                }
             /*   if (!authorized && !"CONNECT".equals(messageType) &&  !"UNSUBSCRIBE".equals(messageType) && !"DISCONNECT".equals(messageType)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }*/
                return message;
            }
        });
    }

    private String getSessionIdFromMessage(Message<?> message) {
        return (String) message.getHeaders().get("simpSessionId");
    }

    private String getTokenFromMessage(Message<?> message) {
        return Optional.of(message.getHeaders())
                .flatMap(messageHeaders -> Optional.ofNullable(((Map<String, List<String>>) messageHeaders.get("nativeHeaders"))))
                .flatMap(nativeHeaders -> Optional.ofNullable(nativeHeaders.get("Authorization")))
                .filter(nativeHeader -> !nativeHeader.isEmpty())
                .map(nativeHeader -> nativeHeader.get(0))
                .orElse(null);
    }
}
