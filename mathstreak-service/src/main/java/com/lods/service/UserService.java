package com.lods.service;

import com.lods.domain.dto.UserLoginDTO;
import com.lods.domain.dto.UserRegisterDTO;
import com.lods.domain.res.UserRegOrLoginRes;

public interface UserService {
    UserRegOrLoginRes login(UserLoginDTO userLoginDTO);

    UserRegOrLoginRes register(UserRegisterDTO userRegisterDTO);
}
