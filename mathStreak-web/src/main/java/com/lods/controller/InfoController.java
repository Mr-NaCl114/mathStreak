package com.lods.controller;

import com.lods.common.constants.Constants;
import com.lods.common.response.Response;
import com.lods.domain.dto.SubmitDTO;
import com.lods.service.InfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/game/")
public class InfoController {

    @Resource
    private InfoService infoService;

//    @GetMapping("state")
//    public Response<Object> getState() {
//        return Response.builder()
//                .code(Constants.ResponseCode.SUCCESS.getCode())
//                .info(Constants.ResponseCode.SUCCESS.getMsg())
//                .data(infoService.getGameState())
//                .build();
//    }

    @GetMapping("current-question")
    public Response<Object> currentQuestion() throws Exception{
        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(infoService.getQuestion())
                .build();
    }

    @PostMapping("submit")
    public Response<Object> submit(@RequestBody SubmitDTO submitDTO){
        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(infoService.submit(submitDTO))
                .build();
    }
}
