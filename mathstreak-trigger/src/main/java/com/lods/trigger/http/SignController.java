package com.lods.trigger.http;

import com.lods.api.response.Response;
import com.lods.domain.sign.service.ISignService;
import com.lods.types.common.constants.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/uylite/")
public class SignController implements com.lods.api.SignController {

    @Resource
    private ISignService signService;

    @Override
    @PostMapping("sign_build")
    public Response<Object> signBuild(@RequestBody Map<String, Object> body) {
        Map<String, String> stringMap = new java.util.HashMap<>();
        if (body != null) {
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                stringMap.put(entry.getKey(), entry.getValue() == null ? null : String.valueOf(entry.getValue()));
            }
        }

        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(signService.signBuild(stringMap))
                .build();
    }
}
