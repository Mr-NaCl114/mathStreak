package com.lods.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AIAnswerReqInfo {

    private Integer type;
    private Integer questionId;
}
