package com.lods.service.Impl;

import com.lods.domain.dto.UserLoginDTO;
import com.lods.domain.dto.UserRegisterDTO;
import com.lods.domain.po.User;
import com.lods.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean login(UserLoginDTO userLoginDTO) {
        return true;
    }

    @Override
    public boolean register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);

//        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        return true;
    }
}
