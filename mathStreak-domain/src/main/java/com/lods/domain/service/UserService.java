package com.lods.domain.service;

import com.lods.api.dto.UserLoginDTO;
import com.lods.api.dto.UserRegisterDTO;

public interface UserService {
    boolean login(UserLoginDTO userLoginDTO);

    boolean register(UserRegisterDTO userRegisterDTO);
}
