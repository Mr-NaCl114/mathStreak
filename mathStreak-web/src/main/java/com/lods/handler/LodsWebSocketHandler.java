package com.lods.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lods.common.constants.Constants;
import com.lods.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class LodsWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("新连接建立: {}", session.getId());
        sessionMap.put(session.getId(), session);
        log.info("seesion: {} , {}", session.getId(), session.getRemoteAddress());
        session.sendMessage(new TextMessage("连接完成"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("收到消息: {}", message.getPayload());
        session.sendMessage(new TextMessage("服务器收到: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("连接关闭: {}", session.getId());
        sessionMap.remove(session.getId());
    }

    public void sendMessage(Object message) throws IOException {
        log.info("发送消息： {}", message);
        log.info("当前在线连接数：{}", sessionMap.size());
        for (WebSocketSession session : sessionMap.values()) {
            String json = new ObjectMapper().writeValueAsString(Response.builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getMsg())
                    .data(message)
                    .build());
            session.sendMessage(new TextMessage(json));
        }
    }
}

