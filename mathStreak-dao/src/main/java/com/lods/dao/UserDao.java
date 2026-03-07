package com.lods.dao;

import com.lods.domain.po.User;
import com.lods.domain.po.UserAnswer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    UserAnswer getUserInfo(Integer userId);

    int userRegister(User user);

    int QueryUserByAccount(String account);
}
