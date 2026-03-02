package com.lods.controller;

import com.lods.common.constants.Constants;
import com.lods.common.response.Response;
import com.lods.domain.dto.SubmitDTO;
import com.lods.service.QuestionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/game/")
public class InfoController {

    @Resource
    private QuestionService questionService;

    @GetMapping("current-question")
    public Response<Object> currentQuestion() throws Exception{
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
