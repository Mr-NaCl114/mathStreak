package com.lods.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;
    private String account;
    private String nickname;
    private String password;
    private Integer exp;
    private String TotalResponse;
    private String RightResponse;
    private Date updateTime;
    private Date createTime;
}
