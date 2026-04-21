package com.lods.api;

import com.lods.api.response.Response;

import java.util.Map;

public interface SignController {

    Response<Object> signBuild(Map<String, Object> body);

}