package com.lods.controller;

import com.lods.common.constants.Constants;
import com.lods.common.response.Response;
import com.lods.domain.dto.UserLoginDTO;
import com.lods.domain.dto.UserRegisterDTO;
import com.lods.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/account/")
public class AccountController {

    @Resource
    private UserService userService;

    @PostMapping("login")
    public Response<Object> login(@RequestBody UserLoginDTO userLoginDTO) throws Exception {
        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(userService.login(userLoginDTO))
                .build();
    }

    @PostMapping("register")
    public Response<Object> register(@RequestBody UserRegisterDTO userRegisterDTO) throws Exception {
        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(userService.register(userRegisterDTO))
                .build();
    }

}
