package com.lods.service.Impl;

import com.lods.common.constants.Constants;
import com.lods.dao.UserDao;
import com.lods.domain.dto.UserLoginDTO;
import com.lods.domain.dto.UserRegisterDTO;
import com.lods.domain.po.User;
import com.lods.domain.res.UserRegOrLoginRes;
import com.lods.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserDao userDao;

    @Override
    public boolean login(UserLoginDTO userLoginDTO) {
        return true;
    }

    @Override
    public UserRegOrLoginRes register(UserRegisterDTO userRegisterDTO) {
        log.info("用户注册信息： {},{}", userRegisterDTO.getAccount(), userRegisterDTO.getNickname());
        if(userDao.QueryUserByAccount(userRegisterDTO.getAccount()) != 0){
            log.info("注册失败，账号已存在： {}", userRegisterDTO.getAccount());
            return UserRegOrLoginRes.builder()
                    .code(Constants.UserRegisterStatus.USERNAME_EXISTS.getCode())
                    .message(Constants.UserRegisterStatus.USERNAME_EXISTS.getMessage())
                    .build();
        }
        if( userDao.userRegister(User.builder()
                .account(userRegisterDTO.getAccount())
                .nickname(userRegisterDTO.getNickname())
                .password(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .exp(Constants.UserRegisterInfo.EXP.getCode())
                .totalResponse(Constants.UserRegisterInfo.TOTAL_RESPONSE.getCode()) // 初始化默认值
                .rightResponse(Constants.UserRegisterInfo.RIGHT_RESPONSE.getCode()) // 初始化默认值
                .build()) >= 1){
            return UserRegOrLoginRes.builder()
                    .code(Constants.UserRegisterStatus.SUCCESS.getCode())
                    .message(Constants.UserRegisterStatus.SUCCESS.getMessage())
                    .build();
        }
        return UserRegOrLoginRes.builder()
                .code(Constants.UserRegisterStatus.FAILURE.getCode())
                .message(Constants.UserRegisterStatus.FAILURE.getMessage())
                .build();
    }
}
