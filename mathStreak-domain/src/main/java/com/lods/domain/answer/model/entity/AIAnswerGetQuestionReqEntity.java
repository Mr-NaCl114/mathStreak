package com.lods.domain.answer.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIAnswerGetQuestionReqEntity {

    private Integer type;
    private Integer questionId;
    private String sign;
}
