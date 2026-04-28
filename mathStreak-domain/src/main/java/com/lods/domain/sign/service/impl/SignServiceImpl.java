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
        log.info("sign构建，字段: {}", params);
        return SignBuild.generateSign(params);
    }
}

