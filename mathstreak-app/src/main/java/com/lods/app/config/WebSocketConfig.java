package com.lods.app.config;

import com.lods.trigger.listener.LodsWebSocketHandlerListener;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private LodsWebSocketHandlerListener lodsWebSocketHandlerListener;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(lodsWebSocketHandlerListener, "/ws/game")
                .setAllowedOrigins("*");
    }
}
