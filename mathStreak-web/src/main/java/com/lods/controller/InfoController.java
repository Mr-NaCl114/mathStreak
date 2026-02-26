package com.lods.controller;

import com.lods.common.constants.Constants;
import com.lods.common.response.Response;
import com.lods.service.InfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/game/")
public class InfoController {

    @Resource
    private InfoService infoService;

    @GetMapping("state")
    public Response<Object> getState() {
        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(infoService.getGameState())
                .build();
    }

    @GetMapping("current-question")
    public Response<Object> currentQuestion(){
        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(infoService.getQuestion())
                .build();
    }
}
