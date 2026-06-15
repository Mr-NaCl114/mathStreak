package com.lods.domain.sign.service.impl;

import com.lods.domain.sign.service.ISignService;
import com.lods.types.common.util.SignBuild;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class SignServiceImpl implements ISignService {
    @Override
    public String signBuild(Map<String, String> params) {

        if (params.containsKey("code") && params.containsKey("tel")) {
            log.info("sign构建，尝试登录，字段: \"userId\":\"{}\"，\"tel\":\"{}\"", params.get("userId"), params.get("tel"));
        } else if (params.containsKey("deviceInfId") && params.containsKey("token")) {
            log.info("sign构建，开启设备，字段: \"userId\":\"{}\"，ON", params.get("userId"));
        } else if (params.containsKey("orderNum") && params.containsKey("tableName")) {
            log.info("sign构建，关闭设备，字段: \"userId\":\"{}\"，OFF", params.get("userId"));
        }

        return SignBuild.generateSign(params);
    }
}

