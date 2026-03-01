package com.lods.domain.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRes {

    private Integer type;
    private Integer questionId;
    private String description;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private Integer difficultyLevel;
}
