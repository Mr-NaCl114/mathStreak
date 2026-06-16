package com.lods.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AIAnswerReqInfoDTO {

    private Integer type;
    private Integer questionId;
    private String sign;
}
