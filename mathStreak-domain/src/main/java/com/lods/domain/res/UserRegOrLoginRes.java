package com.lods.domain.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegOrLoginRes {
    private int code; // 0 成功，1 失败
    private String message; // 错误信息或成功提示
}
