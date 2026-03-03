package com.lods.service;

import com.lods.domain.dto.UserLoginDTO;
import com.lods.domain.dto.UserRegisterDTO;

public interface UserService {
    boolean login(UserLoginDTO userLoginDTO);

    boolean register(UserRegisterDTO userRegisterDTO);
}
