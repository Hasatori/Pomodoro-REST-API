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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final UserService userService;
    private final JwtTokenUtil tokenUtil;
    public static final HashMap<String,String> USER_TOKENS=new HashMap<>();

    public WebSocketConfig(UserService userService, JwtTokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/pomodoro", "/group","/user");
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
                List<String> authorization = ((Map<String, List<String>>) message.getHeaders().get("nativeHeaders")).get("Authorization");
                String messageType = ((SimpMessageType) message.getHeaders().get("simpMessageType")).name();

                boolean authorized = false;
                if (authorization != null) {
                    String token = authorization.get(0);
                    if (token != null) {
                        User user = userService.getUserFromToken(token);
                        // if token is valid configure Spring Security to manually set authentication
                        if (tokenUtil.validateToken(token, user) && accessor != null) {
                            authorized = true;
                            accessor.setUser(user);
                            USER_TOKENS.put(user.getUsername(),token);
                        }
                    }
                }
                if (!authorized && !"UNSUBSCRIBE".equals(messageType) && !"DISCONNECT".equals(messageType)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }
                return message;
            }
        });
    }
}
