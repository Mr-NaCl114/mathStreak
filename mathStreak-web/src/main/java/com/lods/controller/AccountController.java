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
        if (userService.login(userLoginDTO)) {
            return Response.builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getMsg())
                    .build();
        }
        return Response.builder()
                .code(Constants.ResponseCode.UN_ERROR.getCode())
                .info(Constants.ResponseCode.UN_ERROR.getMsg())
                .build();
    }

    @GetMapping("register")
    public Response<Object> register(@RequestBody UserRegisterDTO userRegisterDTO) throws Exception {
        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(userService.register(userRegisterDTO))
                .build();
    }

}
