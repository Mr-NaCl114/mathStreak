package com.lods.domain.answer.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIAnswerGetQuestionEntity {

    private String description;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private String answer;
}
