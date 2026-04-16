package com.lods.trigger.http;

import com.lods.types.common.constants.Constants;
import com.lods.api.response.Response;
import com.lods.api.dto.SubmitDTO;
import com.lods.trigger.listener.LodsWebSocketHandler;
import com.lods.domain.service.QuestionService;
import com.lods.domain.service.StatusService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/game/")
public class QuestionController {

    @Resource
    private QuestionService questionService;
    @Resource
    private StatusService statusService;
    @Resource
    private LodsWebSocketHandler lodsWebSocketHandler;

    @GetMapping("current-question")
    public Response<Object> currentQuestion() throws Exception{
        lodsWebSocketHandler.sendMessage(statusService.updateStatus());
        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(questionService.getQuestion())
                .build();
    }

    @PostMapping("submit")
    public Response<Object> submit(@RequestBody SubmitDTO submitDTO){
        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(questionService.submit(submitDTO))
                .build();
    }
}
