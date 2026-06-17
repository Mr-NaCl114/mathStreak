package com.lods.trigger.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lods.api.response.Response;
import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.service.IStatusService;
import com.lods.types.common.constants.Constants;
import com.lods.types.common.enums.ResponseCode;
import jakarta.annotation.Resource;
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
public class LodsWebSocketHandlerListener extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Resource
    private IStatusService IStatusService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        log.info("新连接建立: {}, 远程IP：{}", session.getId(), session.getRemoteAddress());
        log.info("新连接建立, 远程IP：{}", session.getRemoteAddress());

        //  +1当前连接人数
        sessionMap.put(session.getId(), session);
        IStatusService.updateCurrentAnswer(CurrentAnswerChangeEntity.builder()
                .isAdd(Constants.CurrentAnswerChange.ADD.getCode())
                .build());

        //  发送信息
        session.sendMessage(new TextMessage("连接完成"));
        sendMessage(IStatusService.getCurrentStatus());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("收到消息: {}", message.getPayload());
        session.sendMessage(new TextMessage("服务器收到: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("连接关闭, 远程IP：{}", session.getRemoteAddress());

        //  -1当前连接人数
        sessionMap.remove(session.getId());
        IStatusService.updateCurrentAnswer(CurrentAnswerChangeEntity.builder()
                .isAdd(Constants.CurrentAnswerChange.REDUCE.getCode())
                .build());

        //  发送信息
        sendMessage(IStatusService.getCurrentStatus());
    }

    public void sendMessage(Object message) throws IOException {
        log.info("发送消息： {}", message);
        //  log.info("当前在线连接数：{}", sessionMap.size());
        for (WebSocketSession session : sessionMap.values()) {
            String json = new ObjectMapper().writeValueAsString(Response.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(message)
                    .build());
            session.sendMessage(new TextMessage(json));
        }
    }
}

